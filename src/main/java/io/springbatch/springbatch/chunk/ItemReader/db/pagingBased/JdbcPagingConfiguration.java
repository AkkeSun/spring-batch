package io.springbatch.springbatch.chunk.ItemReader.db.pagingBased;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JdbcPagingConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Customer2ItemWriter itemWriter;

    private final DataSource dataSource;

    private int chunkSize = 3;


    @Bean
    public Job jdbcPagingJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbPagingStep())
                .build();
    }

    @Bean
    public Step jdbPagingStep() throws Exception {
        return stepBuilderFactory.get("jdbPagingStep")
                .<Customer2, Customer2>chunk(chunkSize)
                .reader(jdbcPagingItemReader())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public ItemReader <Customer2> jdbcPagingItemReader() throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("name", "s%");
        params.put("age", 30);

        return new JdbcPagingItemReaderBuilder<Customer2>()
                .name("jdbcPagingItemReader")
                .pageSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Customer2.class)) // rowMapper 등록
                .queryProvider(createQueryProvider())    // 쿼리 등록
                .parameterValues(params)
                .build();

    }

    private PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);          // dataSource
        queryProvider.setSelectClause("name, age, year"); // select
        queryProvider.setFromClause("from customer");     // from
        queryProvider.setWhereClause("where age > :age and name like :name"); // where

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("age", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys); // sort ( 필수 )

        return queryProvider.getObject();
    }
}
