package com.sparrow.spark.utils;

import com.sparrow.spark.feature.TransformerDateConfig;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

import java.util.List;

public class HiveHelper {
    private SparkSession session;

    public HiveHelper(SparkSession session) {
        this.session = session;
    }

    public void writePartition(TransformerDateConfig config, Dataset<Row> ds, String tableName, List<String> columnList) {
        String partitionColumn = "dt";
        String transformedTableName = tableName + "_transformer";
        if(config.isTest()){
            transformedTableName="test_mall."+transformedTableName;
        }
        String columns[] = new String[columnList.size()];
        String header = columnList.get(0);
        columnList.set(0, partitionColumn);
        columnList.toArray(columns);

        if (config.isOverwrite()) {
            ds.select(header, columns).write().mode(SaveMode.Overwrite).partitionBy(partitionColumn).saveAsTable(transformedTableName);
            return;
        }
        String tableNameOfHive = session.sql("show tables like '" + transformedTableName + "'").first().getAs("tableName");
        if (tableNameOfHive.equalsIgnoreCase(transformedTableName)) {
            Long partitionCount = session.sql("select count(0) from " + transformedTableName + " where " + partitionColumn + "=" + config.getDate()).first().getLong(0);
            //幂等检测
            if (partitionCount > 0) {
                session.sql("alter table " + transformedTableName + " drop partition(" + partitionColumn + "=" + config.getDate() + ")");
            }
            ds.select(header, columns).write().mode(SaveMode.Append).partitionBy(partitionColumn).saveAsTable(transformedTableName);
        } else {
            ds.select(header, columns).write().mode(SaveMode.Overwrite).partitionBy(partitionColumn).saveAsTable(transformedTableName);
        }
    }
}
