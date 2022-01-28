package io.springbatch.springbatch.chunk.itemWriter.db.jpa;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JpaConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final JpaItemProcessor processor;

    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 3;


    @Bean
    public Job jpaJob() {
        return jobBuilderFactory.get("jpaJob")
                .incrementer(new RunIdIncrementer())
                .start(jpaStep())
                .build();
    }

    @Bean
    public Step jpaStep() {
        return stepBuilderFactory.get("jpaStep")
                .<CustomerEntity, CustomerEntityBackup>chunk(chunkSize)
                .reader(jpaReader())
                .processor(processor) // 컨버팅
                .writer(jpaWriter())
                .build();
    }

    private ItemReader<CustomerEntity> jpaReader() {

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("age", 30);
        parameter.put("name", "s%");

        return new JpaPagingItemReaderBuilder<CustomerEntity>()
                .name("jpaReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)   // 페이지 사이즈
                .queryString("select c from CustomerEntity c where c.age > :age and c.name like :name")
                .parameterValues(parameter)
                .build();
    }


    private ItemWriter<? super CustomerEntityBackup> jpaWriter() {
        return new JpaItemWriterBuilder<CustomerEntityBackup>()
          //      .usePersist(true) // entity 를 persist 할 것인지 여부. false 면 merge. 기본값 true
                .entityManagerFactory(entityManagerFactory)
                .build();
    }



}
