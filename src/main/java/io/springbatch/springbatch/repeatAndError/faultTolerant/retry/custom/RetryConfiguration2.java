 package io.springbatch.springbatch.repeatAndError.faultTolerant.retry.custom;

 import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.RetryException;
 import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.basic.RetryItemProcessor;
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
 import org.springframework.retry.backoff.FixedBackOffPolicy;
 import org.springframework.retry.policy.SimpleRetryPolicy;
 import org.springframework.retry.support.RetryTemplate;

 import java.util.HashMap;
 import java.util.Map;


 /**
  *  RetryTemplate 을 이용한 custom setting 사용하기
  */
@Configuration
@RequiredArgsConstructor
public class RetryConfiguration2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final RetryItemProcessor2 retryItemProcessor2;

    @Bean
    public Job retryJob_(){
        return jobBuilderFactory.get("retryJob_")
                .incrementer(new RunIdIncrementer())
                .start(retryStep_())
                .build();
    }


    @Bean
    public Step retryStep_(){
        return stepBuilderFactory.get("retryStep_")
                .<String, Customer>chunk (5)
                .reader(new ItemReader<String>() {
                    int num = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        num ++;
                        return num > 20 ? null : String.valueOf(num);
                    }
                })
                .processor(retryItemProcessor2) // retryTemplate 을 상속받은 process
                .writer(items -> items.forEach(System.out::println))
                .faultTolerant()
                .build();
    }


    @Bean
    public RetryTemplate retryTemplate(){

        // 예외 대상 클래스 입력
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(RetryException.class, true);
        SimpleRetryPolicy simpleRetryPolicy= new SimpleRetryPolicy(2, exceptionClass); // 빈복횟수, 예외대상

        // 재시도를 할 때 시간을 지연할 수 있도록 돕는 객채
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(2000); //ms

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(simpleRetryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

}
