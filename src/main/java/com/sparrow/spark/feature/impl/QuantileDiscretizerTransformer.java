package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.QuantileDiscretizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class QuantileDiscretizerTransformer implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) {
        String output=inputCol + "_quantile";
        QuantileDiscretizer discretizer = new QuantileDiscretizer();
        discretizer.setInputCol(inputCol);
        discretizer.setOutputCol(output);
        Integer bucketNum = Integer.valueOf(args);
        discretizer.setNumBuckets(bucketNum);
        return new TransformerResult(discretizer.fit(ds).transform(ds),output);
    }
}
