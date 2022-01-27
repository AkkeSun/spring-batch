package io.springbatch.springbatch.chunk.ItemReader.itemReaderAdaptor;

import io.springbatch.springbatch.chunk.ItemReader.CustomerEntity;
import io.springbatch.springbatch.chunk.ItemReader.CustomerEntityItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 이미 존재하는 Service 를 가져와 사용하는 방법
 */
@Configuration
@RequiredArgsConstructor
public class AdaptorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final AdaptorService adaptorService;

    private int chunkSize = 3;


    @Bean
    public Job adaptorJob() {
        return jobBuilderFactory.get("adaptorJob")
                .incrementer(new RunIdIncrementer())
                .start(adaptorStep())
                .build();
    }

    @Bean
    public Step adaptorStep() {
        return stepBuilderFactory.get("adaptorStep")
                .<CustomerEntity, CustomerEntity>chunk(chunkSize)
                .reader(itemReaderAdapter())
                .writer(adaptorItemWriter())
                .build();
    }

    @Bean
    public ItemReaderAdapter itemReaderAdapter() {
        ItemReaderAdapter adapter = new ItemReaderAdapter();

        adapter.setTargetObject(adaptorService);
        adapter.setTargetMethod("testMethod");

        return adapter;
    }

    @Bean
    public ItemWriter<CustomerEntity> adaptorItemWriter() {
        return items -> {
            System.out.println(items);
        };

    }

}
