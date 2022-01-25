package io.springbatch.springbatch.jobAndFlow;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 기본예제
 */
@Configuration
@RequiredArgsConstructor
public class JobAndFlowConfiguration1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flowJob(){
        return jobBuilderFactory.get("flowJob")
                .incrementer(new RunIdIncrementer())

                // ====== 처음 시작하는 step 혹은 flow ======
                .start(flow1_sf())

                // ====== 다음으로 이동할 step 혹은 flow ======
                .next(step2_sf())

                // ====== on : 위 step 의 status 정의  : COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN -> step listener 를 통해 사용자 정의 가능
                // to : 결과가 true 면 실행할 step 정의  ======
                .on("COMPLETED").to(step3_sf())

                // ====== from : 위의 Step or Flow 를 추가적으로 정의 ======
                .from(step2_sf())

                .on("FAILED").to(step1_sf())


                // ====== JOB 을 처리하는  있는 API ======
                // .stop() STOPPED
                // .fail() FAILED
                // .end()  COMPLETED
                // .stopAndRestart() STOPPED 과 기본 동일 -> 직전의 flow 는 COMPLETED 처리,  이후는 STOPPED 처리


                // ====== build 앞에 위치하면 FlowBuilder 를 종료하고 SimpleFlow 객채 생성 ======
                .end()

                // ====== FlowJob 생성하고 Flow 필드에 SimpleFlow 저장  ======
                .build();

    }

    @Bean
    public Step step1_sf(){
        return stepBuilderFactory.get("step1_sf")
                .tasklet(( (stepContribution, chunkContext) -> {
                    System.out.println("step1_sf");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step2_sf(){
        return stepBuilderFactory.get("step2_sf")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step2_sf");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step3_sf(){
        return stepBuilderFactory.get("step3_sf")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step3_sf");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step step4_sf(){
        return stepBuilderFactory.get("step4_sf")
                .tasklet(((stepContribution, chunkContext) -> {
                    System.out.println("step4_sf");
                    throw new RuntimeException("err");
                }))
                .build();
    }



    @Bean
    public Flow flow1_sf(){
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow1_sf");
        flowFlowBuilder.start(step1_sf())
                .next(step2_sf())
                .end();
        return flowFlowBuilder.build();
    }

}
