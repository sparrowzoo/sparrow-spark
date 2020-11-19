package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.Bucketizer;
import org.apache.spark.sql.Dataset;

public class BucketSpliter implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset ds, String inputCol, String args) {
        Bucketizer bucketizer = new Bucketizer();
        bucketizer.setInputCol(inputCol);
        bucketizer.setOutputCol(inputCol + "_bucket_spliter");
        String[] argArray = args.split(",");
        double[] actualSplits = new double[argArray.length];
        for (int i = 0; i < argArray.length; i++) {
            actualSplits[i] = Double.valueOf(argArray[i]);
        }
        bucketizer.setSplits(actualSplits);
        return new TransformerResult(bucketizer.transform(ds), bucketizer.getOutputCol());
    }
}
