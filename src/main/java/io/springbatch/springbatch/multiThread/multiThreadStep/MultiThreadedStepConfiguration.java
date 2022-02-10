 package io.springbatch.springbatch.multiThread.multiThreadStep;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import io.springbatch.springbatch.multiThread.asyncItemProcessor.StopWatchJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 /**
  * MultiThreadStep 을 통한 처리
  * Test : Single Thread & Multi Thead 속도 비교. 현재 어떤 Thread 가 동작하는지 확인
  * JpaPaging 보다 JdbcPaging 이 성능이 더 좋다
  */
 @Configuration
 @RequiredArgsConstructor
 public class MultiThreadedStepConfiguration {

     private final JobBuilderFactory jobBuilderFactory;
     private final StepBuilderFactory stepBuilderFactory;
     private final EntityManagerFactory entityManagerFactory;
     private final DataSource dataSource;
     private final int chunkSize = 50;

     /**
      * =====================================
      *      총 소요시간 : 16766
      * =====================================
      */
     @Bean
     public Job jpaMultiThreadedJob() throws Exception {
         return jobBuilderFactory.get("jpaMultiThreadedJob")
                 .incrementer(new RunIdIncrementer())
                 .start(jpaMultiThreadStep())
                 //.start(singleThreadStep())
                 .listener(new StopWatchJobListener())
                 .build();
     }

     /**
      * =====================================
      *      총 소요시간 : 62875
      * =====================================
      */
     @Bean
     public Job jpaSingleThreadedJob() throws Exception {
         return jobBuilderFactory.get("jpaSingleThreadedJob")
                 .incrementer(new RunIdIncrementer())
                 .start(jpaSingleThreadStep())
                 .listener(new StopWatchJobListener())
                 .build();
     }
     /**
      =====================================
            총 소요시간 : 16578
      =====================================
      */
     @Bean
     public Job jdbcMultiThreadedJob() throws Exception {
         return jobBuilderFactory.get("jdbcMultiThreadedJob")
                 .incrementer(new RunIdIncrementer())
                 .start(jdbcMultiThreadStep())
                 .listener(new StopWatchJobListener())
                 .build();
     }

     /**
      =====================================
            총 소요시간 : 62766
      =====================================
      */
     @Bean
     public Job jdbcSingleThreadedJob() throws Exception {
         return jobBuilderFactory.get("jdbcSingleThreadedJob")
                 .incrementer(new RunIdIncrementer())
                 .start(jdbcSingleThreadStep())
                 .listener(new StopWatchJobListener())
                 .build();
     }


     @Bean
     public Step jdbcMultiThreadStep() throws Exception {
         return stepBuilderFactory.get("jdbcMultiThreadStep")
                 .<CustomerEntity, Integer>chunk (chunkSize)
                 .reader(jdbcMultiThreadTestReader())
                 .listener(new CustomItemReaderListener())
                 .processor(multiThreadTestProcessor())
                 .listener(new CustomItemProcessorListener())
                 .writer(multiThreadTestWriter())
                 .listener(new CustomItemWriterListener())
                 .taskExecutor(taskExecutor())// MultiThreadStep 설정
                 .build();
     }

     @Bean
     public Step jdbcSingleThreadStep() throws Exception {
         return stepBuilderFactory.get("jdbcSingleThreadStep")
                 .<CustomerEntity, Integer>chunk (chunkSize)
                 .reader(jdbcMultiThreadTestReader())
                 .listener(new CustomItemReaderListener())
                 .processor(multiThreadTestProcessor())
                 .listener(new CustomItemProcessorListener())
                 .writer(multiThreadTestWriter())
                 .listener(new CustomItemWriterListener())
                 .build();
     }

     @Bean
     public Step jpaMultiThreadStep() throws Exception {
         return stepBuilderFactory.get("jpaMultiThreadStep")
                 .<CustomerEntity, Integer>chunk (chunkSize)
                 .reader(jpaMultiThreadTestReader())
                 .listener(new CustomItemReaderListener())
                 .processor(multiThreadTestProcessor())
                 .listener(new CustomItemProcessorListener())
                 .writer(multiThreadTestWriter())
                 .listener(new CustomItemWriterListener())
                 .taskExecutor(taskExecutor())// MultiThreadStep 설정
                 .build();
     }

     @Bean
     public Step jpaSingleThreadStep() throws Exception {
         return stepBuilderFactory.get("jpaSingleThreadStep")
                 .<CustomerEntity, Integer>chunk (chunkSize)
                 .reader(jpaMultiThreadTestReader())
                 .listener(new CustomItemReaderListener())
                 .processor(multiThreadTestProcessor())
                 .listener(new CustomItemProcessorListener())
                 .writer(multiThreadTestWriter())
                 .listener(new CustomItemWriterListener())
                 .build();
     }


     @Bean
     public TaskExecutor taskExecutor() {
         ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
         taskExecutor.setThreadNamePrefix("async-thread"); // Thread 이름 설정
         taskExecutor.setCorePoolSize(4); // 기본적으로 생성할 Thread 갯수 
         taskExecutor.setMaxPoolSize(8);  // 처음 생성된 Thread 로 처리되지 않은 작업이 있을 때 얼마만큼의 Thread 를 더 생성할지 설정 
         return taskExecutor;
     }


     @Bean
     public ItemReader<CustomerEntity> jpaMultiThreadTestReader() {

         return new JpaPagingItemReaderBuilder<CustomerEntity>()
                 .name("jpaItemReader")
                 .entityManagerFactory(entityManagerFactory)
                 .pageSize(chunkSize)
                 .queryString("select c from CustomerEntity c")
                 .build();
     }

     @Bean
     public ItemReader<CustomerEntity> jdbcMultiThreadTestReader() throws Exception {

         return new JdbcPagingItemReaderBuilder<Customer2>()
                 .name("jdbcPagingItemReader")
                 .pageSize(chunkSize)
                 .dataSource(dataSource)
                 .rowMapper(new BeanPropertyRowMapper(CustomerEntity.class)) // rowMapper 등록
                 .queryProvider(queryProvider())    // 쿼리 등록
                 .build();
     }


     @Bean
     public PagingQueryProvider queryProvider() throws Exception {
         SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
         queryProvider.setDataSource(dataSource);           // dataSource
         queryProvider.setSelectClause("id, name, age, year"); // select
         queryProvider.setFromClause("from customer_entity");     // from

         Map<String, Order> sortKeys = new HashMap<>();
         sortKeys.put("id", Order.ASCENDING);
         queryProvider.setSortKeys(sortKeys); // sort ( 필수 )

         return queryProvider.getObject();
     }

     @Bean
     public ItemProcessor<CustomerEntity, Integer> multiThreadTestProcessor() {
         return new ItemProcessor<CustomerEntity, Integer>() {

             @Override
             public Integer process(CustomerEntity customerEntity) throws Exception {
                 Thread.sleep(100);
                 return customerEntity.getId() + 100;
             }
         };
     }

      @Bean
      public ItemWriter<Integer> multiThreadTestWriter() {
          return new ItemWriter<Integer>() {
              @Override
              public void write(List<? extends Integer> list) throws Exception {
                  System.out.println(list);
              }
          };
      }

 }

