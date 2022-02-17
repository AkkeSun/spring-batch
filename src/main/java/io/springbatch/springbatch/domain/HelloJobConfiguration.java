package io.springbatch.springbatch.domain;

import io.springbatch.springbatch.domain.listener.JobRepositoryListener;
import io.springbatch.springbatch.domain.tasklet.ExecutionTasklet1;
import io.springbatch.springbatch.domain.tasklet.ExecutionTasklet2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 실행순서 : Job 구동 -> Step 실행 -> Step 안에 있는 Tasklet 실행
 */
@Configuration
@RequiredArgsConstructor // 초기화되지 않은 final 필드나 @NonNull 이 붙은 필드에 대해 생성자 생성
public class HelloJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ExecutionTasklet1 executionTasklet1;
    private final ExecutionTasklet2 executionTasklet2;
    private final JobRepositoryListener jobRepositoryListener;

    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")     // jobName Setting
                .start(step1()) // 스탭 실행
                .next(step2())  // 스탭 실행
                .listener(jobRepositoryListener) // 리스너 등록
                .build();
    }

    // Step 생성
    @Bean
    public Step step1(){
        return  stepBuilderFactory.get("step1")
                .tasklet(executionTasklet1)     // tasklet 등록
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(executionTasklet2)
                .build();
    }
}

