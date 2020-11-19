package com.sparrow.spark;

import com.sparrow.spark.feature.impl.LogTransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class LogTransformerTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);

        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer bucketSpliter = new LogTransformer(spark);
        Dataset<Row> result = bucketSpliter.transform(inputData, "feature", "log-10");
        System.out.println("Log output with buckets Log");
        result.show();
        spark.close();
    }
}
