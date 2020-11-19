package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class PCATransformer implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) {
        Integer k = Integer.valueOf(args);
        String output = inputCol + "-pca";
        PCA pca = new PCA()
                .setInputCol(inputCol)
                .setOutputCol(output)
                .setK(k);
        return new TransformerResult(pca.fit(ds).transform(ds), output);
    }
}
