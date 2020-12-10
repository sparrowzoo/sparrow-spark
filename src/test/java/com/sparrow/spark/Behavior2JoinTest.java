package com.sparrow.spark;

import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Behavior2JoinTest {
    public static void main(String[] args) throws AnalysisException {
        SparkSession spark = SparkSessionAccessor.getSession("feature", SparkSessionAccessor.MASTER_LOCAL);
        Dataset<Row> userBehavior = FeatureSampleData.getBehavior(spark);
        userBehavior.createTempView("user_behavior");
        //spark.sql("select * from user_behavior l left join user_behavior r on l.uid=r.uid and l.id+1=r.id").show();
        spark.sql("SELECT uid, concat_ws(',',collect_set(sid)) FROM user_behavior GROUP BY uid ").show();
        spark.sql("select uid,sid,t,(row_number() over(PARTITION BY uid order by uid,t)) as num from user_behavior").show();
        String sql = "select uid,sid,t," +
                "lag(t,1,0) over (partition by uid order by t) as pre_time," +
                "(t-(lag(t,1,'0') over (partition by uid order by t))) as sess," +
                "lag(sid,1,'0') over (partition by uid order by t) as pre_sid," +
                "lead(sid,1,'0') over (partition by uid order by t) as next_sid from user_behavior";
        spark.sql(sql).show();
    }
}
