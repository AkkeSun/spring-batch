package io.springbatch.springbatch.chunk.ItemReader.db.cursorBased;

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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JpaCursorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final CustomerEntityItemWriter itemWriter;

    private final EntityManagerFactory entityManagerFactory;

    private int chunkSize = 3;


    @Bean
    public Job jpaCursorJob() {
        return jobBuilderFactory.get("jpaCursorJob")
                .incrementer(new RunIdIncrementer())
                .start(jpaCursorStep())
                .build();
    }

    @Bean
    public Step jpaCursorStep() {
        return stepBuilderFactory.get("jpaCursorStep")
                .<CustomerEntity, CustomerEntity>chunk(chunkSize)
                .reader(jpaCursorItemReader())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader<CustomerEntity> jpaCursorItemReader() {

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("age", 30);
        parameter.put("name", "s%");

        // 매핑할 데이터를 Entity 로 만들어야한다
        return new JpaCursorItemReaderBuilder<CustomerEntity>()
                .name("jpaItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from CustomerEntity c where c.age > :age and c.name like :name")
                .parameterValues(parameter)
                .build();
    }

}
