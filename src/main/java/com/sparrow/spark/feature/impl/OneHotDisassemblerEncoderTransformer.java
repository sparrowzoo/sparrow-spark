package com.sparrow.spark.feature.impl;

import com.sparrow.spark.feature.TransformerResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.ArrayList;
import java.util.List;

public class OneHotDisassemblerEncoderTransformer extends OneHotEncoderTransformer {
    private SparkSession session;

    public OneHotDisassemblerEncoderTransformer(SparkSession session) {
        this.session = session;
    }

    @Override
    public TransformerResult transform(Dataset<Row> ds, String inputCol, String args) throws Exception {
        TransformerResult onehotAssembler = super.transform(ds, inputCol, args);
        Vector result = onehotAssembler.getDs().first().getAs(onehotAssembler.getOutput());
        //(3,[],[])
        if (result==null||result.size()==0) {
            throw new RuntimeException("result is null");
        }
        String tempTableName=inputCol+"_onehot_temp";
        //注册names表
        onehotAssembler.getDs().registerTempTable(tempTableName);
        Integer onehotCount =result.size();
        List<String> columns = new ArrayList<>();
        List<String> udfColumnList=new ArrayList<>();
        for (int i = 1; i <= onehotCount; i++) {
            String alias=inputCol + i;
            columns.add(alias);
            udfColumnList.add("onehot("+onehotAssembler.getOutput()+","+(i-1)+")as "+alias);
        }
        //session.sql("select *,onehot(label_onehot,1)as label1 from labelonehot_temp").show();
        Dataset<Row> onehotDisassemble= session.sql("select *,"+StringUtils.join(udfColumnList,",")+" from "+tempTableName);
        return new TransformerResult(onehotDisassemble, columns);
    }
}
