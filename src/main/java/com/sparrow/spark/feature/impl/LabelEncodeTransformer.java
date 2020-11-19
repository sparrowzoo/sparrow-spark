package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class LabelEncodeTransformer implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) {
        StringIndexer stringIndexer = new StringIndexer();
        stringIndexer.setInputCol(inputCol);
        stringIndexer.setOutputCol(inputCol + "_label");
        return new TransformerResult(stringIndexer.fit(ds).transform(ds), stringIndexer.getOutputCol());
    }
}
