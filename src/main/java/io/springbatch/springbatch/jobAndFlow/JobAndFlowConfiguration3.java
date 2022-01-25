package io.springbatch.springbatch.jobAndFlow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JobExecutionDecider 활용
 */
@Configuration
@RequiredArgsConstructor
public class JobAndFlowConfiguration3 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob3(){
        return jobBuilderFactory.get("flowJob3")
                .incrementer(new RunIdIncrementer())
                .start(step1___sf())
                .next(decider())
                .from(decider()).on("EVEN").to(evenStep())
                .from(decider()).on("ODD").to(oddStep())
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider decider(){
        return new CustomDecider();
    }



    @Bean
    public Step step1___sf(){
        return stepBuilderFactory.get("step1__sf")
                .tasklet(( (stepContribution, chunkContext) -> {
                    System.out.println("step1");
               //     stepContribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step evenStep(){
        return stepBuilderFactory.get("evenStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("짝수");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step oddStep(){
        return stepBuilderFactory.get("oddStep")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("홀수");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
