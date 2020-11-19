package com.sparrow.spark;

import com.sparrow.spark.feature.FeatureTransformer;
import com.sprucetec.recommend.feature.feature.impl.*;
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.api.java.*;
import org.apache.spark.sql.types.DataTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public class TransformerRegistry implements Serializable {
    public static final String NULL = "NULL";
    public static final String BINARIZER = "BINARIZER";
    public static final String BUCKET_SPLITER = "BUCKET_SPLITER";
    public static final String LABEL_ENCODE = "LABEL_ENCODE";
    public static final String ONEHOT_ENCODER = "ONEHOT_ENCODER";
    public static final String ONEHOT_DISASSEMBLER_ENCODER = "ONEHOT_DISASSEMBLER_ENCODER";
    public static final String QUANTILE_DISCRETIZER = "QUANTILE_DISCRETIZER";
    public static final String VECTOR_ASSEMBLER = "VECTOR_ASSEMBLER";
    public static final String LOG = "LOG";
    public static final String MAX_ABS_SCALER = "MAX_ABS_SCALER";
    public static final String STANDARD_SCALER = "STANDARD_SCALER";


    private Map<String, FeatureTransformer> container = new HashMap<>();

    private TransformerRegistry() {
    }

    static class Inner {
        static TransformerRegistry registry = new TransformerRegistry();
    }

    public FeatureTransformer getTransformer(String key) {
        return this.container.get(key);
    }

    public static TransformerRegistry getInstance() {
        return Inner.registry;
    }

    public void init(SparkSession sparkSession) {
        this.container.put(BINARIZER, new BinarizerTransformer());
        this.container.put(BUCKET_SPLITER, new BucketSpliter());
        this.container.put(LABEL_ENCODE, new LabelEncodeTransformer());
        this.container.put(ONEHOT_ENCODER, new OneHotEncoderTransformer());
        this.container.put(QUANTILE_DISCRETIZER, new QuantileDiscretizerTransformer());
        this.container.put(VECTOR_ASSEMBLER, new VectorAssemblerTransformer());
        this.container.put(LOG, new LogTransformer());
        this.container.put(STANDARD_SCALER, new StandardScalerTransformer(sparkSession));
        this.container.put(ONEHOT_DISASSEMBLER_ENCODER, new OneHotDisassemblerEncoderTransformer(sparkSession));
        this.container.put(MAX_ABS_SCALER, new MaxAbsScalerTransformer(sparkSession));
        sparkSession.udf().register("onehot", new UDF2<SparseVector, Integer, Integer>() {
            @Override
            public Integer call(SparseVector key, Integer index) {
                return (int) key.apply(index);
            }
        }, DataTypes.IntegerType);

        sparkSession.udf().register("log-e", new UDF1<Double, Integer>() {
            @Override
            public Integer call(Double key) {
                if (key == 0D) {
                    return 0;
                }
                return (int) Math.log(key);
            }
        }, DataTypes.IntegerType);

        sparkSession.udf().register("log-10", new UDF1<Double, Integer>() {
            @Override
            public Integer call(Double key) {
                if (key == 0D) {
                    return 0;
                }
                return (int) Math.log10(key);
            }
        }, DataTypes.IntegerType);

        sparkSession.udf().register("scaler", new UDF5<Double, Integer, Integer, BigDecimal, BigDecimal, Double>() {

            @Override
            public Double call(Double x, Integer maxScaler, Integer minScaler, BigDecimal maxValue, BigDecimal minValue) throws Exception {
                BigDecimal k = new BigDecimal(maxScaler - minScaler).divide(maxValue.subtract(minValue), 6, BigDecimal.ROUND_HALF_UP);
                return minScaler + k.doubleValue() * (x - minValue.doubleValue());
            }
        }, DataTypes.DoubleType);

        sparkSession.udf().register("standard_scaler", new UDF3<Double, BigDecimal, BigDecimal, Double>() {
            @Override
            public Double call(Double x, BigDecimal mean, BigDecimal standard) throws Exception {
                return (x - mean.doubleValue()) / standard.doubleValue();
            }
        }, DataTypes.DoubleType);
    }

    public List<String> getKeys() {
        return new ArrayList<>(this.container.keySet());
    }
}
