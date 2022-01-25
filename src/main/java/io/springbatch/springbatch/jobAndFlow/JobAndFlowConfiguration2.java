package io.springbatch.springbatch.jobAndFlow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 복잡한 트랜지션 예제
 */
@Configuration
@RequiredArgsConstructor
public class JobAndFlowConfiguration2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob2(){
        return jobBuilderFactory.get("flowJob2")
                .incrementer(new RunIdIncrementer())
                .start(step1__sf())
                    .on("FAILED")
                    .to(step2__sf())     // step1() == FAILED 면 step2를 실행하라
                    .on("*")
                    .stop()              // step2() 를 완료하면 상태와 상관없이 Job 을 중단하라
                .from(step1__sf())
                    .on("*")
                    .to(step3__sf())     // step1() != FAILED 이면 step3를 실행하라
                .next(step4__sf())
                    .on("FAILED")
                    .to(step2__sf())     // step4() == FAILED 이면 step2 를 실행하라
                    .on("PASS")
                    .end()               // step4() == PASS 이면 flow 를 종료하라 (사용자 지정 상태)
                .end()
                .build();
    }

    @Bean
    public Step step1__sf(){
        return stepBuilderFactory.get("step1__sf")
                .tasklet(( (stepContribution, chunkContext) -> {
                    System.out.println("step1");
               //     stepContribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step2__sf(){
        return stepBuilderFactory.get("step2__sf")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step2");
                    stepContribution.setExitStatus(ExitStatus.FAILED);
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step3__sf(){
        return stepBuilderFactory.get("step3__sf")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step3");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step4__sf(){
        return stepBuilderFactory.get("step4__sf")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step4");
                    return RepeatStatus.FINISHED;
                }))
                .listener(new PassCheckingListener())// 사용지 정의 상태코드
                .build();
    }

}
