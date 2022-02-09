 package io.springbatch.springbatch.repeatAndError.faultTolerant.retry.basic;

 import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.RetryException;
 import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.custom.RetryItemProcessor2;
 import lombok.RequiredArgsConstructor;
 import org.springframework.batch.core.Job;
 import org.springframework.batch.core.Step;
 import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
 import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
 import org.springframework.batch.core.launch.support.RunIdIncrementer;
 import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
 import org.springframework.batch.core.step.skip.SkipPolicy;
 import org.springframework.batch.item.ItemReader;
 import org.springframework.batch.item.NonTransientResourceException;
 import org.springframework.batch.item.ParseException;
 import org.springframework.batch.item.UnexpectedInputException;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.retry.RetryPolicy;
 import org.springframework.retry.policy.SimpleRetryPolicy;

 import java.util.HashMap;
 import java.util.Map;


 /**
  *  retry 만 주는 경우, 오류 발생 시 처음 Chunk 로 가서 재시도를 하기 때문에  오류구문을 계속 로드하게된다. skip 을 함께 주어야한다
  */
@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final RetryItemProcessor2 retryItemProcessor2;

    @Bean
    public Job retryJob(){
        return jobBuilderFactory.get("retryJob")
                .incrementer(new RunIdIncrementer())
                .start(retryStep())
                .build();
    }


    @Bean
    public Step retryStep(){
        return stepBuilderFactory.get("retryStep")
                .<String, String>chunk (5)
                .reader(new ItemReader<String>() {
                    int num = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        num ++;
                        return num > 20 ? null : String.valueOf(num);
                    }
                })
                .processor(new RetryItemProcessor())
                .writer(items -> items.forEach(System.out::println))
                .faultTolerant()
                /*
                .skip(RetryException.class)
                .skipLimit(3)
                .retry(RetryException.class)
                .retryLimit(3)
                */

                .skipPolicy(skipPolicy())
                .retryPolicy(retryPolicy())

                /*
                .backOffPolicy() // 다시 Retry 하기까지의 지연시간 (ms)
                .noRetry() // 예외 발생 시 Retry 하지 않을 예외타입 설정
                .noRollback() //  예외 발싱 시 Rollback 하지 않을 예외 타입 설정
                 */
                .build();
    }


     @Bean
     public SkipPolicy skipPolicy (){
         Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
         exceptionClass.put(RetryException.class, true); // 예외 대상 클래스 입력

         LimitCheckingItemSkipPolicy itemSkipPolicy = new LimitCheckingItemSkipPolicy(3, exceptionClass);  // 반복횟수, 예외대상
         return itemSkipPolicy;
     }

    @Bean
    public RetryPolicy retryPolicy (){
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(RetryException.class, true); // 예외 대상 클래스 입력
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, exceptionClass); // 반복횟수, 예외대상
        return retryPolicy;
    }

}
