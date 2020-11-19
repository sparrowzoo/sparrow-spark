package com.sparrow.spark.feature;

import java.util.List;

public class TransformerDateConfig {
    private Integer date;
    private List<TransformerConfig> configs;
    private boolean isOverwrite =false;
    private boolean isTest=false;

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public List<TransformerConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<TransformerConfig> configs) {
        this.configs = configs;
    }

    public boolean isOverwrite() {
        return isOverwrite;
    }

    public void setOverwrite(boolean overwrite) {
        this.isOverwrite = overwrite;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setTest(boolean test) {
        isTest = test;
    }
}
