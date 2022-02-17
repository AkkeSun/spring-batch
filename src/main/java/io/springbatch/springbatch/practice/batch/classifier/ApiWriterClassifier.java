package io.springbatch.springbatch.practice.batch.classifier;

import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ApiWriterClassifier implements Classifier<ApiRequestVo, ItemWriter<? super ApiRequestVo>> {

    private Map<String, ItemWriter<ApiRequestVo>> writerMap = new HashMap<>();

    public void SetProcessorMap( Map<String, ItemWriter<ApiRequestVo>> writerMap){
        this.writerMap = writerMap;
    }

    @Override
    public  ItemWriter<ApiRequestVo> classify(ApiRequestVo apiRequestVo) {
        return writerMap.get(apiRequestVo.getProductVo().getType());
    }
}
/*
public class ApiWriterClassifier<C, T> implements Classifier<C, T> {

    private Map<String, ItemWriter<ApiRequestVo>> writerMap = new HashMap<>();

    public void SetProcessorMap( Map<String, ItemWriter<ApiRequestVo>> writerMap){
        this.writerMap = writerMap;
    }

    @Override
    public T classify(C classifier) {
        return (T)writerMap.get(((ApiRequestVo)classifier).getProductVo().getType());
    }
}
 */