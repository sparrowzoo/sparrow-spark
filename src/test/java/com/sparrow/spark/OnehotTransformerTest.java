package com.sparrow.spark;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sparrow.spark.feature.impl.OneHotDisassemblerEncoderTransformer;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class OnehotTransformerTest {
    public static void main(String[] args) throws Exception {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);
        Dataset<Row> inputData = FeatureSampleData.get(spark);
        FeatureTransformer featureTransform = new OneHotDisassemblerEncoderTransformer(spark);
        Dataset<Row> result = featureTransform.transform(inputData, "label", "").getDs();
        System.out.println("label onehot ");
        result.show();
//        spark.sql("show databases").show();
//        spark.sql("show tables").show();
//        spark.sql("drop table label_onehot_test");
//
//        result.write().saveAsTable("`label_onehot_test`");
//        spark.sql("desc `label_onehot_test`").show();
    }
}
