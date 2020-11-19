package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.Binarizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class BinarizerTransformer implements FeatureTransformer {
    public TransformerResult transform(Dataset ds, String inputCol, String args) {
        double threshold = Double.valueOf(args);
        Binarizer binarizer = new Binarizer();
        binarizer.setThreshold(threshold);
        binarizer.setInputCol(inputCol);
        binarizer.setOutputCol(inputCol+"_binarizer");
        Dataset<Row> result= binarizer.transform(ds);
        return new TransformerResult(result,binarizer.getOutputCol());
    }
}
