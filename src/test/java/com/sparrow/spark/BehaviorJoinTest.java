package com.sparrow.spark;

import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class BehaviorJoinTest {
    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);
        Dataset<Row> userBehavior = FeatureSampleData.getBehavior(spark);
        userBehavior.createTempView("user_behavior");
        spark.sql("select * from user_behavior l left join user_behavior r on l.uid=r.uid and l.id+1=r.id").show();
    }
}
