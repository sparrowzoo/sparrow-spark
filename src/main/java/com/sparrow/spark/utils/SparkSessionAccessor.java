package com.sparrow.spark.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkSessionAccessor {
    public static final String MASTER_LOCAL = "local[*]";
    public static final String MASTER_YARN = "yarn";

    public static SparkSession getSession(String appName, String master) {
        return getSession(appName, null, master, null, null);
    }

    public static SparkSession getSession(String appName, String memory, String master, Integer core, Integer instances) {
        if (StringUtils.isEmpty(appName)) {
            appName = "spark";
        }
        if (StringUtils.isEmpty(memory)) {
            memory = "4G";
        }
        if (StringUtils.isEmpty(master)) {
            master = SparkSessionAccessor.MASTER_LOCAL;
        }
        if (core == null) {
            core = Runtime.getRuntime().availableProcessors();
        }
        if (instances == null) {
            instances = 2;
        }
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.app.name", appName);
        sparkConf.set("spark.executor.memory", memory);
        sparkConf.set("spark.master", master);
        sparkConf.set("spark.executor.cores", core + "");
        sparkConf.set("spark.executor.instances", instances + "");

        if (MASTER_LOCAL.equalsIgnoreCase(master)) {
            return SparkSession
                    .builder()
                    .config(sparkConf)
                    .enableHiveSupport()
                    .appName(appName)
                    .getOrCreate();
        }
        return SparkSession
                .builder()
                .enableHiveSupport()
                .appName(appName)
                .getOrCreate();
    }
}
