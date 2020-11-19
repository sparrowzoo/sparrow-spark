package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class StandardScalerTransformer implements FeatureTransformer {
    private SparkSession session;

    public StandardScalerTransformer(SparkSession session) {
        this.session = session;
    }

    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) throws Exception {
        ds.registerTempTable("ds_temp_standard_scaler");
        Object avg = session.sql("SELECT avg(" + inputCol + ") as mean FROM ds_temp_standard_scaler").first().get(0);
        Object stand = session.sql("SELECT stddev(" + inputCol + ") as standard FROM ds_temp_standard_scaler").first().get(0);
        String output = inputCol + "_standard_scaler";
        Dataset<Row> result = session.sql("select *,standard_scaler(cast(" + inputCol + " as double)," + avg + "," + stand + ")as" + output + " from ds_temp_standard_scaler");
        return new TransformerResult(result, output);
    }
}
