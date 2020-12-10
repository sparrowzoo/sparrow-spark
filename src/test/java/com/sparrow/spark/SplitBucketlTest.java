package com.sparrow.spark;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.impl.BucketSpliter;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SplitBucketlTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);

        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer bucketSpliter = new BucketSpliter();
        String buckets = "-0.5,0,0.5,1";
        Dataset<Row> result = bucketSpliter.transform(inputData, "feature", buckets).getDs();
        System.out.println("Bucketizer output with buckets= " + buckets);
        result.show();
        spark.close();
    }
}
