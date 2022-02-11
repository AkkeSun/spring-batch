package io.springbatch.springbatch.listener.chunk;


import io.springbatch.springbatch.listener.chunk.listener.ChunkListenerTest;
import io.springbatch.springbatch.listener.chunk.listener.ItemProcessListenerTest;
import io.springbatch.springbatch.listener.chunk.listener.ItemReaderListenerTest;
import io.springbatch.springbatch.listener.chunk.listener.ItemWriterListenerTest;
import io.springbatch.springbatch.listener.jobAndStep.interfaceType.JobListenerTest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * ChunkListener _ beforeChunk 
 *   ItemReaderListener _ beforeRead : 아이템을 한 건씩 읽기 전에 호출
 *   ItemReaderListener _ afterRead : 아이템 호출이 성공될 때 마다 호출
 *   ItemProcessListener _ beforeProcess
 *   ItemProcessListener _ afterProcess
 *   ItemProcessWriter _ beforeWriter
 *   ItemProcessWriter _ afterWriter
 *  ChunkListener - afterChunk
 *  ChunkListener _ beforeChunk : Null Check
 *  ChunkListener - afterChunk
 */


@Configuration
@RequiredArgsConstructor
public class ChunkListenerConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job ChunkListenerJob(){
        return jobBuilderFactory.get("ChunkListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(ChunkListenerStep())
                .listener(new JobListenerTest())
                .build();
    }

    @Bean
    public Step ChunkListenerStep(){
        return stepBuilderFactory.get("ChunkListenerStep")
                .<Integer, Integer>chunk(5)
                .reader(new ItemReaderTest())
                .listener(new ItemReaderListenerTest())
                .processor(new ItemProcessorTest())
                .listener(new ItemProcessListenerTest())
                .writer(new ItemWriterTest())
                .listener(new ItemWriterListenerTest())
                .listener(new ChunkListenerTest())    // chunkListener
                .build();
    }


}
