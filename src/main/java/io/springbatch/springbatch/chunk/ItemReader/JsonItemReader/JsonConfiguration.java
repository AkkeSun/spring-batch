package io.springbatch.springbatch.chunk.ItemReader.JsonItemReader;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * JSON 데이터 파싱
 */
@RequiredArgsConstructor
@Configuration
public class JsonConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Customer2ItemWriter itemWriter;

    @Bean
    public Job jsonJob() {
        return jobBuilderFactory.get("jsonJob")
                .incrementer(new RunIdIncrementer())
                .start(json_step())
                .build();
    }

    @Bean
    public Step json_step() {
        return stepBuilderFactory.get("json_step")
                .<Customer2, Customer2>chunk(5)
                .reader(jsonItemReader())
                .writer(itemWriter)
                .build();
    }

    private ItemReader <? extends Customer2> jsonItemReader() {
        return new JsonItemReaderBuilder<Customer2>()
                .name("jsonItemReader")
                .resource(new ClassPathResource("customer.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Customer2.class))
                .build();
    }


}
