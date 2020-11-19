package com.sparrow.spark;

import com.sparrow.spark.feature.impl.QuantileDiscretizerTransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class QuantileDiscretizerSplitBucketTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);

        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer bucketSpliter = new QuantileDiscretizerTransformer();
        String buckets = "8";
        Dataset<Row> result = bucketSpliter.transform(inputData, "feature", buckets);
        System.out.println("QuantileDiscretizerSplitBucket output with buckets= " + buckets);
        result.sort("feature-quantile-discretizer").show(1000);
        spark.close();
    }
}
