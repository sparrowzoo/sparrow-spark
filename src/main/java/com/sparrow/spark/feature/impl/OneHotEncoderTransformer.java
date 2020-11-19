package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.TransformerResult;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class OneHotEncoderTransformer implements FeatureTransformer {
    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) throws Exception {
        String labelIndex = inputCol + "_index";
        String outpurCol=inputCol + "_onehot";
        StringIndexer stringIndexer = new StringIndexer().setInputCol(inputCol).setOutputCol(labelIndex);
        Dataset labelDataset = stringIndexer.fit(ds).transform(ds);
        OneHotEncoder oneHotEncoder = new OneHotEncoder();
        oneHotEncoder.setInputCol(labelIndex);
        oneHotEncoder.setOutputCol(outpurCol);
        return new TransformerResult(oneHotEncoder.transform(labelDataset),outpurCol);
    }
}
