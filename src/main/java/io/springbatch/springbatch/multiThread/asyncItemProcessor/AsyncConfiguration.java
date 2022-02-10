 package io.springbatch.springbatch.multiThread.asyncItemProcessor;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.List;
import java.util.function.Function;

/**
 * AsyncItemProcessor : 하나의 Step 안에서 ItemProcessor 가 비동기적으로 동작하는 구조
 * TEST : Single Tread (기존방식) 와 MultiThread 방식의 작업 시간을 비교해보기
 */
@Configuration
@RequiredArgsConstructor
public class AsyncConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job asyncJob() throws Exception {
        return jobBuilderFactory.get("asyncJob")
                .incrementer(new RunIdIncrementer())
                .start(syncStep())
            //    .start(asyncStep())
                .listener(new StopWatchJobListener())
                .build();
    }


    // Single Thread 방식의 Step
    @Bean
    public Step syncStep(){
        return stepBuilderFactory.get("syncStep")
                .<String, String>chunk (100)
                .reader(commonItemReader())
                .processor(syncProcessor())
                .writer(syncWriter())
                .build();
    }


     // MultiThread 방식의 Step
    @Bean
    public Step asyncStep() throws Exception {
        return stepBuilderFactory.get("asyncStep")
                .<String, String>chunk (100)
                .reader(commonItemReader())
                .processor(asyncProcessor())
                .writer(asyncWriter())
                .build();
    }

    @Bean
    public ItemReader<String> commonItemReader(){
        return new ItemReader<String>() {
            int i = 0;
            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                i++;
                return i>100 ? null : String.valueOf(i);
            }
        };
    }

    @Bean
    public ItemProcessor<String, String> syncProcessor() {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String item) throws Exception {
                Thread.sleep(30);
                int data = Integer.parseInt(item) + 100;
                return "data Convert : " + data;
            }
        };
    }

     @Bean
     public ItemWriter<String> syncWriter() {
         return new ItemWriter<String>() {
             @Override
             public void write(List<? extends String> list) throws Exception {
                 System.out.println(list);
             }
         };
     }

    @Bean
    public AsyncItemProcessor asyncProcessor() throws Exception {
        // 비동기 실행 환경을 제공하는 클래스
        AsyncItemProcessor <String, String> asyncItemProcessor = new AsyncItemProcessor<>();
        // 실제 사용할 동기 방식의 process 등록
        asyncItemProcessor.setDelegate(syncProcessor());
        // thread 설정
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        // bean 으로 등록하지 않는경우 해줘야할 셋업
        // asyncItemProcessor.afterPropertiesSet();
        return asyncItemProcessor;
    }
    @Bean
    public AsyncItemWriter asyncWriter() {
        // 비동기 실행 환경을 제공하는 클래스
        AsyncItemWriter<String> asyncItemWriter = new AsyncItemWriter<>();
        // 실제 사용할 동기 방식의 writer 등록
        asyncItemWriter.setDelegate(syncWriter());
        return asyncItemWriter;
    }



}
