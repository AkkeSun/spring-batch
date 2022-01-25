package io.springbatch.springbatch.jobScopeAndStepScope;

import io.springbatch.springbatch.jobScopeAndStepScope.listener.ScopeJobListener;
import io.springbatch.springbatch.jobScopeAndStepScope.listener.ScopeStepListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class ScopeConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job scopeJob(){
        return jobBuilderFactory.get("scopeJob")
                .incrementer(new RunIdIncrementer())
                .listener(new ScopeJobListener()) // 리스너를 통해 JobExecutionContext 전달
                .start(scopeStep1(null)) // 컴파일 시 오류가 걸리지 않게 기본값 null 을 준다
                .next(scopeStep2())
                .build();
    }

    @Bean
    @JobScope // proxy 객채 자동 생성
    public Step scopeStep1(@Value("#{jobParameters['message1']}") String message){
        System.out.println("message1 : " + message);
        return stepBuilderFactory.get("scopeStep1")
                .tasklet(scopeTasklet1(null))
                .build();
    }


    @Bean
    public Step scopeStep2(){
        return stepBuilderFactory.get("scopeStep2")
                .tasklet(scopeTasklet2(null))
                .listener(new ScopeStepListener()) // 리스너를 통해 stepExecutionContext 전달
                .build();
    }

    @Bean
    @StepScope
    public Tasklet scopeTasklet1(@Value("#{jobExecutionContext['message2']}") String message2){
        System.out.println("message2 :" + message2);

        return (stepContribution, chunkContext) -> {
            System.out.println(">>>>>> scopeTasklet1");
            return  RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope // proxy 객채 자동 생성
    public Tasklet scopeTasklet2(@Value("#{stepExecutionContext['message3']}") String message3){
        System.out.println("message3 :" + message3 );

        return (stepContribution, chunkContext) -> {
            System.out.println(">>>>>> scopeTasklet2");
            return  RepeatStatus.FINISHED;
        };
    }

}
