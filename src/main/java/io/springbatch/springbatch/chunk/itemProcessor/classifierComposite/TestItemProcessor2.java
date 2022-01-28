package io.springbatch.springbatch.chunk.itemProcessor.classifierComposite;

import org.springframework.batch.item.ItemProcessor;

public class TestItemProcessor2 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    @Override
    public ProcessorInfo process(ProcessorInfo processorInfo) throws Exception {

        System.out.println(">>> ItemProcessor 2 ");
        return processorInfo;
    }
}
