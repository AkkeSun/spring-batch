package io.springbatch.springbatch.chunk.itemWriter.db.jdbc;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JdbcConfiguration {


    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Customer2ItemWriter itemWriter;


    private int chunkSize = 3;

    private final DataSource dataSource;


    @Bean
    public Job jdbcJob() throws Exception {
        return jobBuilderFactory.get("jdbcJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbcStep())
                .build();
    }

    @Bean
    public Step jdbcStep() throws Exception {
        return stepBuilderFactory.get("jdbcStep")
                .<Customer2, Customer2>chunk(chunkSize)
                .reader(jdbcPagingItemReader2())
                .writer(jdbcItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<Customer2> jdbcItemWriter() {
        return new JdbcBatchItemWriterBuilder<Customer2>()
                .dataSource(dataSource)
                .sql(" insert into customer_backup values (:name, :age, :year) ")
                .beanMapped()
                .build();
    }

    @Bean
    public ItemReader <Customer2> jdbcPagingItemReader2() throws Exception {

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("name, age, year"); // select
        queryProvider.setFromClause("from customer");     // from
        queryProvider.setWhereClause("where age > :age and name like :name"); // where

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("age", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "s%");
        params.put("age", 30);

        JdbcPagingItemReader <Customer2> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setFetchSize(chunkSize);
        reader.setRowMapper(new BeanPropertyRowMapper<>(Customer2.class));
        reader.setParameterValues(params);
        reader.setQueryProvider(queryProvider);

        return reader;
    }

}
