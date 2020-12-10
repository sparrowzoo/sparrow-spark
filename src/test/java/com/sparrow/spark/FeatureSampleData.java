package com.sparrow.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FeatureSampleData {
    public static Dataset<Row> getBehavior(SparkSession session) {
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            long t = i * Duration.ofMinutes(10).toMillis();
            data.add(RowFactory.create(i, i / 3, i, System.currentTimeMillis() - t));
        }
        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("uid", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("sid", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("t", DataTypes.LongType, false, Metadata.empty())
        });
        return session.createDataFrame(data, schema);
    }

    public static Dataset<Row> get(SparkSession spark) {
        List<Row> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add(RowFactory.create(i, new Random().nextDouble(), "label" + i));
        }
        StructType schema = new StructType(new StructField[]{
                new StructField("id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("feature", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("label", DataTypes.StringType, false, Metadata.empty())
        });
        return spark.createDataFrame(data, schema);
    }
}
