package io.springbatch.springbatch.listener.retryAndSkip;

import io.springbatch.springbatch.listener.chunk.ItemReaderTest;
import io.springbatch.springbatch.listener.chunk.ItemWriterTest;
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

@Configuration
@RequiredArgsConstructor
public class RetryAndSkipListenerConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job retryListenerJob(){
        return jobBuilderFactory.get("retryListenerJob")
                .incrementer(new RunIdIncrementer())
                .start(retryListenerStep())
                .listener(new JobListenerTest())
                .build();
    }

    @Bean
    public Step retryListenerStep(){
        return stepBuilderFactory.get("retryListenerStep")
                .<Integer, Integer>chunk(5)
                .reader(new ItemReaderTest())
                .listener(new ItemReaderListenerTest())

                .processor(new RetryItemProcess())
                .listener(new ItemProcessListenerTest())

                .writer(new ItemWriterTest())
                .listener(new ItemWriterListenerTest())

                .faultTolerant()
                .retry(RuntimeException.class)
                .retryLimit(3)
                .listener(new CustomRetryListener())
                .skip(RuntimeException.class)
                .skipLimit(3)
                .listener(new CustomSkipListener())

                .listener(new ChunkListenerTest())    // chunkListener

                .build();
    }

}
