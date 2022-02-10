 package io.springbatch.springbatch.multiThread.partition;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import io.springbatch.springbatch.multiThread.asyncItemProcessor.StopWatchJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

 /**
  * PartitionStep 이 SlaveStep 을 실행시키는 구조
  */
 @Configuration
 @RequiredArgsConstructor
 public class PartitioningConfiguration {

     private final JobBuilderFactory jobBuilderFactory;
     private final StepBuilderFactory stepBuilderFactory;
     private final DataSource dataSource;
     private final int chunkSize = 50;

     @Bean
     public Job partitioningJob() throws Exception {
         return jobBuilderFactory.get("partitioningJob")
                 .incrementer(new RunIdIncrementer())
                 .start(masterStep())
                 .listener(new StopWatchJobListener())
                 .build();
     }


     @Bean
     public Step masterStep() throws Exception {
         return stepBuilderFactory.get("masterStep")
                 .partitioner(slaveStep().getName(), partitioner()) // slaveStep 이름, partitioner 등록
                 .step(slaveStep())    // 사용할 slaveStep 등록 -> Chunk 타입
                 .gridSize(4)          // 몇 개의 파티션으로 나눌지 설정
                 .taskExecutor(new SimpleAsyncTaskExecutor()) // multiThread 구현을 위한 taskExecutor 등록
                 .build();
     }

     @Bean
     public Partitioner partitioner() {
         ColumnRangePartitioner partitioner = new ColumnRangePartitioner();
         partitioner.setColumn("id");
         partitioner.setDataSource(dataSource);
         partitioner.setTable("customer_entity");
         return partitioner;
     }

     @Bean
     public Step slaveStep() throws Exception {
         return stepBuilderFactory.get("slaveStep")
                 .<CustomerEntity, CustomerEntity>chunk (chunkSize)
                 .reader(jdbcPartitioningTestReader(null, null))
                 .writer(jdbcPartitioningTestWriter())
                 .build();
     }

     @Bean
     @StepScope 
     // Partitioner 에서 담은 ExecutionContext 값을 가져온다
     public ItemReader<CustomerEntity> jdbcPartitioningTestReader(
             @Value("#{stepExecutionContext['minValue']}") Long minValue,
             @Value("#{stepExecutionContext['maxValue']}") Long maxValue
     ) throws Exception {

         System.err.println("reading : " + minValue + " to " +maxValue);

         MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
         queryProvider.setSelectClause("id, name, age, year");
         queryProvider.setFromClause("from customer_entity");
         queryProvider.setWhereClause("where id >= " + minValue + " and id <= " + maxValue);

         Map<String, Order> sortKeys = new HashMap<>();
         sortKeys.put("id", Order.ASCENDING);
         queryProvider.setSortKeys(sortKeys);

         JdbcPagingItemReader<CustomerEntity> reader = new JdbcPagingItemReader<>();

         reader.setDataSource(dataSource);
         reader.setFetchSize(chunkSize);
         reader.setRowMapper(new BeanPropertyRowMapper<>(CustomerEntity.class));
         reader.setQueryProvider(queryProvider);

         return reader;
     }

     @Bean
     @StepScope // Runtime 시점에 병렬처리 하려면 붙여줘야한다.
     public ItemWriter<CustomerEntity> jdbcPartitioningTestWriter() {
         return new JdbcBatchItemWriterBuilder<CustomerEntity>()
                 .dataSource(dataSource)
                 .sql(" insert into customer_backup values (:id, :name, :age, :year) ")
                 .beanMapped()
                 .build();
     }

 }
