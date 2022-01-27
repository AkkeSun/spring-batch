package io.springbatch.springbatch.chunk.ItemReader.db.pagingBased;

import io.springbatch.springbatch.chunk.ItemReader.CustomerEntity;
import io.springbatch.springbatch.chunk.ItemReader.CustomerEntityItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JpaPagingrConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final CustomerEntityItemWriter itemWriter;

    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 3;


    @Bean
    public Job jpaPagingJob() {
        return jobBuilderFactory.get("jpaPagingJob")
                .incrementer(new RunIdIncrementer())
                .start(jpaPagingStep())
                .build();
    }

    @Bean
    public Step jpaPagingStep() {
        return stepBuilderFactory.get("jpaPagingStep")
                .<CustomerEntity, CustomerEntity>chunk(chunkSize)
                .reader(jpaPagingItemReader())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader<CustomerEntity> jpaPagingItemReader() {

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("age", 30);
        parameter.put("name", "s%");

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("jpaItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)   // 페이지 사이즈
                .queryString("select c from CustomerEntity c where c.age > :age and c.name like :name")
                .parameterValues(parameter)
                .build();
    }

}
