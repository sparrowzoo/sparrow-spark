package com.sparrow.spark;

import com.sparrow.spark.feature.FeatureTransformerCompositor;
import com.sparrow.spark.feature.TransformerConfig;
import com.sparrow.spark.feature.TransformerDateConfig;
import com.sparrow.spark.utils.SparkSessionAccessor;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * master force_overwrite is_test day
     * local[*] true false 2020115
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Date beforeDay = DateUtils.addDays(new Date(System.currentTimeMillis()), -1);

        String master = SparkSessionAccessor.MASTER_LOCAL;
        if (args != null && args.length > 0 && SparkSessionAccessor.MASTER_YARN.equalsIgnoreCase(args[0])) {
            master = SparkSessionAccessor.MASTER_YARN;
        }

        boolean isOverwrite = false;
        if (args != null && args.length > 1) {
            isOverwrite = Boolean.valueOf(args[1]);
        }

        boolean isTest = false;
        if (args != null && args.length > 2) {
            isTest = Boolean.valueOf(args[2]);
        }

        Integer day = Integer.valueOf(DateFormatUtils.format(beforeDay, "yyyyMMdd"));
        if (args != null && args.length > 3) {
            day = Integer.valueOf(args[3]);
        }
        SparkSession spark = SparkSessionAccessor.getSession("feature", master);

        InputStream inputStream = Main.class.getResourceAsStream("/transformer_config.txt");

        List<String> configs = IOUtils.readLines(inputStream);
        List<TransformerConfig> transformerConfigs = new ArrayList<>();
        for (String config : configs) {
            try {
                TransformerConfig transformerConfig = new TransformerConfig();
                String configArray[] = config.split("\\|");
                //test_feature|feature|BUCKET_SPLITER|-0.5,0,0.5,999999999
                transformerConfig.setTableName(configArray[0]);
                transformerConfig.setColumnName(configArray[1]);
                transformerConfig.setTransformer(configArray[2]);
                if (configArray.length > 3) {
                    transformerConfig.setArgs(configArray[3]);
                }
                transformerConfigs.add(transformerConfig);
            } catch (Throwable throwable) {
                logger.error("transformer config is error line {}", config);
            }
        }

        TransformerDateConfig transformerDateConfig = new TransformerDateConfig();
        transformerDateConfig.setDate(day);
        transformerDateConfig.setTest(isTest);
        transformerDateConfig.setConfigs(transformerConfigs);
        transformerDateConfig.setOverwrite(isOverwrite);
        TransformerRegistry registry = TransformerRegistry.getInstance();
        registry.init(spark);
        FeatureTransformerCompositor transformerCompositor = new FeatureTransformerCompositor(spark);
        transformerCompositor.transform(transformerDateConfig);
    }
}
