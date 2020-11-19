package com.sparrow.spark;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeatureSampleData {
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
