package io.springbatch.springbatch.operator;

import io.springbatch.springbatch.domain.tasklet.ExecutionTasklet1;
import io.springbatch.springbatch.domain.tasklet.ExecutionTasklet2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JobRegistryBeanPostProcessor 를 통해 Job 을 JobRegistry 에 등록
 *
 */
@Configuration
@RequiredArgsConstructor
public class TestJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ExecutionTasklet1 executionTasklet1;
    private final ExecutionTasklet2 executionTasklet2;
    private final JobRegistry jobRegistry;

    @Bean
    public Job operatorJob(){
        return jobBuilderFactory.get("operatorJob")
                .start(operatorStep1())
                .next(operatorStep2())
                .build();
    }

    @Bean
    public Step operatorStep1(){
        return  stepBuilderFactory.get("operatorStep1")
                .tasklet(executionTasklet1)
                .build();
    }

    @Bean
    public Step operatorStep2() {
        return stepBuilderFactory.get("operatorStep1")
                .tasklet(executionTasklet2)
                .build();
    }

    // SpringBatch 가 초기화 될 때 jobRegistry 에 job 들을 등록해주도록 셋팅
    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor(){
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}

