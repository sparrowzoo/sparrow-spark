package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class VectorAssemblerTransformer implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) {
        String[] inputColArray = inputCol.split(",");
        String output="assembler_features";
        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(inputColArray)
                .setOutputCol(output);
        return new TransformerResult(vectorAssembler.transform(ds),output);
    }
}
