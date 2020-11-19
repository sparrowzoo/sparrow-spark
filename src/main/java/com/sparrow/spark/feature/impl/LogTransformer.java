package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import static org.apache.spark.sql.types.DataTypes.DoubleType;

public class LogTransformer implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) throws Exception {
        String base = args;
        String output = inputCol + "_" + base;
        return new TransformerResult(ds.withColumn(output, functions.callUDF(base, ds.col(inputCol).cast(DoubleType))), output);
    }
}
