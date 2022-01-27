package io.springbatch.springbatch.chunk.ItemReader.db.cursorBased;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class JdbcCursorConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Customer2ItemWriter itemWriter;

    private final DataSource dataSource;

    private int chunkSize = 3;


    @Bean
    public Job jdbcCursorJob() {
        return jobBuilderFactory.get("jdbcCursorJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbCursorStep())
                .build();
    }

    @Bean
    public Step jdbCursorStep() {
        return stepBuilderFactory.get("jdbcCursorStep")
                .<Customer2, Customer2>chunk(chunkSize)
                .reader(jdbcCursorItemReader())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader <Customer2> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Customer2>()
                .name("jdbcCursorItemReader")
                .fetchSize(chunkSize)            // fitchSize 는 chunkSize 로 지정
                .sql("select * from customer where age > ? and name like ? order by age desc")
                .beanRowMapper(Customer2.class)   // 매핑할 object 설정
                .queryArguments(30, "s%")         // ? 값 입력
                .dataSource(dataSource)           // sql dataSource 입력
                .build();
    }
}
