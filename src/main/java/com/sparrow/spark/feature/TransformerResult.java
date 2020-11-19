package com.sparrow.spark.feature;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.List;

public class TransformerResult {
    public TransformerResult(Dataset<Row> ds, String output) {
        this.ds = ds;
        this.output = output;
    }

    public TransformerResult(Dataset<Row> ds, List<String> outputs) {
        this.ds = ds;
        this.outputList = outputs;
    }

    private Dataset<Row> ds;
    private String output;
    private List<String> outputList;

    public Dataset<Row> getDs() {
        return ds;
    }

    public void setDs(Dataset<Row> ds) {
        this.ds = ds;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public List<String> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<String> outputList) {
        this.outputList = outputList;
    }
}
