
package io.springbatch.springbatch.chunk.itemWriter.itemWriterAdaptor;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import io.springbatch.springbatch.chunk.ItemReader.itemReaderAdaptor.AdaptorService;
import io.springbatch.springbatch.chunk.itemWriter.db.jpa.CustomerEntityBackup;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 이미 존재하는 Service 를 가져와 사용하는 방법
 */
@Configuration
@RequiredArgsConstructor
public class AdaptorConfiguration2 {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final AdaptorService readAdaptor;

    private final AdaptorService2 writeAdaptor;

    private int chunkSize = 3;


    @Bean
    public Job adaptorJob2() {
        return jobBuilderFactory.get("adaptorJob2")
                .incrementer(new RunIdIncrementer())
                .start(adaptorStep2())
                .build();
    }

    private Step adaptorStep2() {
        return stepBuilderFactory.get("adaptorStep")
                .<CustomerEntity, CustomerEntityBackup>chunk(chunkSize)
                .reader(itemReaderAdapter())
                .writer(adaptorItemWriter())
                .build();
    }

    private ItemReaderAdapter itemReaderAdapter() {
        ItemReaderAdapter adapter = new ItemReaderAdapter();

        adapter.setTargetObject(readAdaptor);
        adapter.setTargetMethod("testMethod");

        return adapter;
    }

    private ItemWriter<CustomerEntity> adaptorItemWriter() {

        ItemWriterAdapter adapter = new ItemWriterAdapter();
        adapter.setTargetObject(writeAdaptor);
        adapter.setTargetMethod("saveMethod");

        return adapter;
    }

}
