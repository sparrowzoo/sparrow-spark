package com.sparrow.spark.feature;

public class TransformerConfig {
    private String tableName;
    private String columnName;
    private String transformer;
    private String args;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTransformer() {
        return transformer;
    }

    public void setTransformer(String transformer) {
        this.transformer = transformer;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
}
