package io.springbatch.springbatch.jobAndStep.step;

import org.springframework.batch.core.step.item.ChunkOrientedTasklet;
import org.springframework.batch.core.step.item.ChunkProcessor;
import org.springframework.batch.core.step.item.ChunkProvider;

public class RunTasklet2 extends ChunkOrientedTasklet {
    public RunTasklet2(ChunkProvider chunkProvider, ChunkProcessor chunkProcessor) {
        super(chunkProvider, chunkProcessor);
    }
}
