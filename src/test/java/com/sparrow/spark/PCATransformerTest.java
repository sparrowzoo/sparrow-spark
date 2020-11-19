package com.sparrow.spark;

import com.sparrow.spark.feature.impl.PCATransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class PCATransformerTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);

        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer featureTransform = new PCATransformer();
        int k = 3;
        Dataset<Row> result = featureTransform.transform(inputData, "feature", k + "");
        System.out.println("PCA output with K = " + k);
        result.show();
    }
}
