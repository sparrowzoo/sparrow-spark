package com.sparrow.spark;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.impl.VectorAssemblerTransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class AssemblerTransforumerTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);

        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer featureTransform = new VectorAssemblerTransformer();
        double threshold = 0.1;
        Dataset<Row> result = featureTransform.transform(inputData, "id,feature","").getDs();
        System.out.println("Binarizer output with Threshold = " + threshold);
        result.show();
    }
}
