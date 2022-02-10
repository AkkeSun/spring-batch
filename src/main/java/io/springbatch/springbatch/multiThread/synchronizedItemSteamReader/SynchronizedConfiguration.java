 package io.springbatch.springbatch.multiThread.synchronizedItemSteamReader;

 import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
 import io.springbatch.springbatch.multiThread.asyncItemProcessor.StopWatchJobListener;
 import lombok.RequiredArgsConstructor;
 import org.springframework.batch.core.ItemReadListener;
 import org.springframework.batch.core.Job;
 import org.springframework.batch.core.Step;
 import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
 import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
 import org.springframework.batch.core.launch.support.RunIdIncrementer;
 import org.springframework.batch.item.ItemReader;
 import org.springframework.batch.item.ItemWriter;
 import org.springframework.batch.item.database.JdbcCursorItemReader;
 import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
 import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
 import org.springframework.batch.item.support.SynchronizedItemStreamReader;
 import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.core.task.TaskExecutor;
 import org.springframework.jdbc.core.BeanPropertyRowMapper;
 import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

 import javax.sql.DataSource;

 /**
  * [ SynchronizedItemSteamReader ]
  * Thread-safe 하지 않은 ItemReader 를 Thread-safe 하게 처리하도록 하는 역할을 한다
  * Spring Batch 4.0 부터 지원한다
  */
 @Configuration
 @RequiredArgsConstructor
 public class SynchronizedConfiguration {

     private final JobBuilderFactory jobBuilderFactory;
     private final StepBuilderFactory stepBuilderFactory;
     private final DataSource dataSource;
     private final int chunkSize = 50;

     @Bean
     public Job synchronizedJob() throws Exception {
         return jobBuilderFactory.get("synchronizedJob")
                 .incrementer(new RunIdIncrementer())
                 .start(synchronizedStep())
                 .listener(new StopWatchJobListener())
                 .build();
     }


     @Bean
     public Step synchronizedStep() throws Exception {
         return stepBuilderFactory.get("synchronizedStep")
                 .<CustomerEntity, CustomerEntity>chunk(chunkSize)
                 //.reader(synchronizedTestReader())
                 .reader(synchronizedTestReader2())
                 .listener(new ItemReadListener<CustomerEntity>() {
                     @Override
                     public void beforeRead() {
                     }

                     @Override
                     public void afterRead(CustomerEntity item) {
                         System.out.println("Thread : " + Thread.currentThread().getName() + " item.getId() : " + item.getId());
                     }

                     @Override
                     public void onReadError(Exception e) {

                     }
                 })
                 .writer(synchronizedTestWriter())
                 .taskExecutor(taskExecutor__())
                 .build();
     }

     // Thread Safe 하지 않은 jdbcCursorItemReader
     @Bean
     public ItemReader<CustomerEntity> synchronizedTestReader() throws Exception {
         return new JdbcCursorItemReaderBuilder<CustomerEntity>()
                 .name("synchronizedTestReader")
                 .fetchSize(chunkSize)
                 .sql("select * from customer_entity")
                 .beanRowMapper(CustomerEntity.class)
                 .dataSource(dataSource)
                 .build();
     }


     // jdbcCursorItemReader ThreadSafe 하도록 SynchronizedItemStreamReader 사용
     @Bean
     public SynchronizedItemStreamReader<CustomerEntity> synchronizedTestReader2() throws Exception {
         JdbcCursorItemReader<CustomerEntity> jdbcCursorItemReader = new JdbcCursorItemReader<>();
         jdbcCursorItemReader.setName("synchronizedTestReader2");
         jdbcCursorItemReader.setFetchSize(chunkSize);
         jdbcCursorItemReader.setSql("select * from customer_entity");
         jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>(CustomerEntity.class));
         jdbcCursorItemReader.setDataSource(dataSource);

         return new SynchronizedItemStreamReaderBuilder<CustomerEntity>()
                 .delegate(jdbcCursorItemReader)
                 .build();
     }

     @Bean
     public ItemWriter<CustomerEntity> synchronizedTestWriter() {
         return new JdbcBatchItemWriterBuilder<CustomerEntity>()
                 .dataSource(dataSource)
                 .sql(" insert into customer_backup values (:id, :name, :age, :year) ")
                 .beanMapped()
                 .build();
     }

     @Bean
     public TaskExecutor taskExecutor__() {
         ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
         taskExecutor.setThreadNamePrefix("async-thread"); // Thread 이름 설정
         taskExecutor.setCorePoolSize(4); // 기본적으로 생성할 Thread 갯수
         taskExecutor.setMaxPoolSize(8);  // 처음 생성된 Thread 로 처리되지 않은 작업이 있을 때 얼마만큼의 Thread 를 더 생성할지 설정
         return taskExecutor;
     }


 }
