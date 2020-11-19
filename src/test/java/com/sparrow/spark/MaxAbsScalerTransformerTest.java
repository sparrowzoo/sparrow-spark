package com.sparrow.spark;

import com.sparrow.spark.feature.impl.MaxAbsScalerTransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class MaxAbsScalerTransformerTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);

        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer featureTransform = new MaxAbsScalerTransformer(spark);
        Dataset<Row> result = featureTransform.transform(inputData, "id", "0,1");
        result.show(1000);
    }
}
