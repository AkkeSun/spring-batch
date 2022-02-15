package io.springbatch.springbatch.practice.batch.classifier;

import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;

import java.util.HashMap;
import java.util.Map;

public class ApiProcessorClassifier implements Classifier<ProductVo, ItemProcessor<?, ? extends ApiRequestVo>> {

    private Map<String, ItemProcessor<ProductVo, ApiRequestVo>> processorMap = new HashMap<>();

    public void SetProcessorMap( Map<String, ItemProcessor<ProductVo, ApiRequestVo>> processorMap){
        this.processorMap = processorMap;
    }

    @Override
    public ItemProcessor<?, ? extends ApiRequestVo> classify(ProductVo productVo) {
        // productVo.getType() 을 key 값으로 가지도록 설정
        return processorMap.get(productVo.getType());
    }
}
