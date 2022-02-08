package io.springbatch.springbatch.repeatAndError.faultTolerant.skip;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

 /**
  * 오류가 발생하더라도 Step 이 즉시 종료되지 않고 진행되도록 셋업
  */
@Configuration
@RequiredArgsConstructor
public class SkipConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job skipJob(){
        return jobBuilderFactory.get("skipJob_")
                .incrementer(new RunIdIncrementer())
                .start(skipStep())
                .build();
    }


    @Bean
    public Step skipStep(){
        return stepBuilderFactory.get("skipStep_")
                .<String, String>chunk (5)

                .reader(new ItemReader<String>() {
                    int num = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        num ++;
                        return num > 20 ? null : String.valueOf(num);
                    }
                })
                .processor(new SkipItemProcessor())
                .writer(new SkipItemWriter())

                // ======== 내결함성 기능 활성화 ========
                .faultTolerant()

                // ======== 예외 발생시 Skip 할 예외 타입 설정 ========
                .skip(SkipException.class)

                // ======== skip 제한 횟수 설정 ========
                .skipLimit(4)

                // ======== skip 을 어떤 조건과 기준으로 적용할 것인지 정책 설정 ========
                //  .skipPolicy()

                // ======== 예외 발생 시 Skip 하지 않을 예외타입 설정 ========
                //  .noSkip()

                .build();
    }

}
