package com.sparrow.spark.feature;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.Serializable;

public interface FeatureTransformer extends Serializable {
    TransformerResult transform(Dataset<Row> ds, String inputCol, String args) throws Exception;
}
