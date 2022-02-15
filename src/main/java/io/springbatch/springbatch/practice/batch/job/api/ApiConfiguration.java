package io.springbatch.springbatch.practice.batch.job.api;

import io.springbatch.springbatch.practice.batch.listener.ApiJobListener;
import io.springbatch.springbatch.practice.batch.tasklet.ApiEndTasklet;
import io.springbatch.springbatch.practice.batch.tasklet.ApiStartTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [ apiJob ]
 * 1. apiStep1 () : apiStartTasklet (log)
 * 2. apiMasterStep() : partitioning Step
 *      - partitioner : 각 type 마다 executionContext Map 생성 후 리턴
 *      - ItemReader : type 으로 잘라서 쿼리 읽어오기
 *      - ItemProcessor :
 * 3. apiStep2 () : apiEndTasklet (log)
 */
@Configuration
@RequiredArgsConstructor
public class ApiConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApiStartTasklet apiStartTasklet;
    private final ApiEndTasklet apiEndTasklet;
    private final Step apiMasterStep;

    @Bean
    public Job apiJob(){
        return jobBuilderFactory.get("apiJob")
                .listener(new ApiJobListener())
                .start(apiStep1())
                .next(apiMasterStep)
                .next(apiStep2())
                .build();
    }

    @Bean
    public Step apiStep1() {
        return stepBuilderFactory.get("apiStep1")
                .tasklet(apiStartTasklet)
                .build();
    }

    @Bean
    public Step apiStep2() {
        return stepBuilderFactory.get("apiStep2")
                .tasklet(apiEndTasklet)
                .build();
    }

}
