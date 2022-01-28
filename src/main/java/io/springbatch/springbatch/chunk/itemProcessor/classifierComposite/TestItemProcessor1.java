package io.springbatch.springbatch.chunk.itemProcessor.classifierComposite;

import org.springframework.batch.item.ItemProcessor;

public class TestItemProcessor1 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo processorInfo) throws Exception {

        System.out.println(">>> ItemProcessor 1 ");
        return processorInfo;
    }
}
