package com.sparrow.spark;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.impl.LabelEncodeTransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class LabelEncodeTransformerTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);
        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer featureTransform = new LabelEncodeTransformer();
        Dataset<Row> result = featureTransform.transform(inputData, "label", "").getDs();
        System.out.println("label transform ");
        result.show();
        spark.sql("drop table test_one_hot_table1");
        spark.sql("drop table test_one_hot_table2");


        //result.write().saveAsTable("test_one_hot_table2");
        //spark.sql("select * from test_one_hot_table2").show();
    }
}
