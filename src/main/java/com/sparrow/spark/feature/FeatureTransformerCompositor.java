package com.sparrow.spark.feature;

import com.sparrow.spark.TransformerRegistry;
import com.sparrow.spark.utils.HiveHelper;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class FeatureTransformerCompositor {
    private Logger logger = LoggerFactory.getLogger(FeatureTransformerCompositor.class);
    private SparkSession sparkSession;

    public FeatureTransformerCompositor(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    private Dataset<Row> fetchDataset(String tableName, Integer partition) {
        return sparkSession.sql("select * from " + tableName + " where dt=" + partition);
    }

    public void transform(TransformerDateConfig dateConfig) throws Exception {
        Set<String> tableNameSet = new HashSet<>();
        TransformerRegistry registry = TransformerRegistry.getInstance();
        registry.init(sparkSession);
        for (TransformerConfig config : dateConfig.getConfigs()) {
            tableNameSet.add(config.getTableName());
        }
        Map<String, Dataset<Row>> tableContainer = new HashMap<>();
        for (String tableName : tableNameSet) {
            tableContainer.put(tableName, this.fetchDataset(tableName, dateConfig.getDate()));
        }

        Map<String, List<String>> columnsOfTable = new HashMap<>();
        for (TransformerConfig config : dateConfig.getConfigs()) {
            Dataset<Row> table = tableContainer.get(config.getTableName());
            if (table == null || table.isEmpty()) {
                logger.error("table " + config.getTableName() + " not found");
                break;
            }

            List<String> columns = columnsOfTable.computeIfAbsent(config.getTableName(), k -> new ArrayList<>());

            if (TransformerRegistry.NULL.equalsIgnoreCase(config.getTransformer())) {
                columns.add(config.getColumnName());
            } else {
                FeatureTransformer featureTransformer = registry.getTransformer(config.getTransformer());
                if (featureTransformer == null) {
                    logger.error("featureTransformer is null {}", config.getTransformer());
                    System.exit(0);
                }
                TransformerResult result = featureTransformer.transform(table, config.getColumnName(), config.getArgs());
                tableContainer.put(config.getTableName(), result.getDs());
//                result.getDs().show();
                if (TransformerRegistry.ONEHOT_DISASSEMBLER_ENCODER.equalsIgnoreCase(config.getTransformer())) {
                    columns.addAll(result.getOutputList());
                } else {
                    columns.add(result.getOutput());
                }
            }
        }

        HiveHelper hiveHelper = new HiveHelper(sparkSession);
        for (String tableName : columnsOfTable.keySet()) {
            Dataset<Row> originDataset = tableContainer.get(tableName);
            if (originDataset.isEmpty()) {
                continue;
            }
            originDataset = originDataset.na().fill(0);
//            tableContainer.get(tableName).show();
            hiveHelper.writePartition(dateConfig, originDataset, tableName, columnsOfTable.get(tableName));
            sparkSession.sql("select * from " + tableName + "_transformer").show();
        }
    }
}
