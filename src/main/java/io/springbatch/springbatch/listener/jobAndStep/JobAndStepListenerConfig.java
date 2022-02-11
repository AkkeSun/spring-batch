package io.springbatch.springbatch.listener.jobAndStep;


import io.springbatch.springbatch.listener.jobAndStep.annotationType.JobListenerTest2;
import io.springbatch.springbatch.listener.jobAndStep.annotationType.StepListenerTest2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobAndStepListenerConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listenerTestJob1(){
        return jobBuilderFactory.get("listenerTestJob1")
                .incrementer(new RunIdIncrementer())
                .start(listenerTestStep1())
             //   .listener(new JobListenerTest())
                .listener(new JobListenerTest2())
                .build();
    }

    @Bean
    public Step listenerTestStep1(){
        return stepBuilderFactory.get("listenerTestStep1")
                .tasklet((configuration, chunkContext)-> RepeatStatus.FINISHED)
            //    .listener(new StepListenerTest())
                .listener(new StepListenerTest2())
                .build();
    }

}
