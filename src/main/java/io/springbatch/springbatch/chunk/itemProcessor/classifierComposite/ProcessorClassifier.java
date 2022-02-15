package io.springbatch.springbatch.chunk.itemProcessor.classifierComposite;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.classify.Classifier;


import java.util.Map;

public class ProcessorClassifier implements Classifier<ProcessorInfo, ItemProcessor<?, ? extends ProcessorInfo>> {

    private Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processMap;

    public void setProcessMap(Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processMap) {
        this.processMap = processMap;
    }

    @Override
    public ItemProcessor<?, ? extends ProcessorInfo> classify(ProcessorInfo processorInfo) {
        // processMap 에서 processorInfo.getId() 를 key 값으로 이용
        return processMap.get(processorInfo.getId());
    }

}
