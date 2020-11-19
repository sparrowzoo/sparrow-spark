package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MaxAbsScalerTransformer implements FeatureTransformer {
    private SparkSession spark;

    public MaxAbsScalerTransformer(SparkSession spark) {
        this.spark = spark;
    }

    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) throws Exception {
        String[] scaler = args.split(",");
        Integer minScaler = Integer.valueOf(scaler[0]);
        Integer maxScaler = Integer.valueOf(scaler[1]);

        ds.registerTempTable("ds_temp_scaler_table");
        Double maxValue = Double.valueOf(spark.sql("SELECT MAX(" + inputCol + ") as maxValue FROM ds_temp_scaler_table").first().get(0).toString());
        Double minValue =Double.valueOf(spark.sql("SELECT MIN(" + inputCol + ") as minValue FROM ds_temp_scaler_table").first().get(0).toString());
        String output = inputCol + "_scaler";
        Dataset<Row> result = spark.sql("select *,scaler(cast(" + inputCol + " as double)," + maxScaler + "," + minScaler + "," + maxValue + "," + minValue + ")as " + output + " from ds_temp_scaler_table");
        return new TransformerResult(result, output);
    }
}

