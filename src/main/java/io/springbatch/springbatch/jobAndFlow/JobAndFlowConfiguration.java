package io.springbatch.springbatch.jobAndFlow;

import io.springbatch.springbatch.jobAndStep.listener.BatchRunJobListener;
import io.springbatch.springbatch.jobAndStep.step.RunTasklet1;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
@RequiredArgsConstructor
public class JobAndFlowConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BatchRunJobListener jobListener;
    private final RunTasklet1 runTasklet1;
    private final JobLauncher jobLauncher;

    @Bean
    public Job flowJob(){
        return jobBuilderFactory.get("flowJob")
                // ====== 처음 시작하는 step 혹은 flow ======
                .start(flow1_sf())

                // ====== 다음으로 이동할 step 혹은 flow ======
                .next(step2_sf())

                // ====== on : 위 step 의 결과 정의  //  to : 결과가 true 면 실행할 step 정의  ======
                .on("COMPLETED").to(step3_sf())

                // ====== from : 이전 단계에서 정의한 Step 의 Flow 를 추가적으로 정의======
                .from(step4_sf())

                .on("FAILED").to(step1_sf())


                // ====== flow를 중지/ 실패 / 종료 / 중지후 재시작 ======
                //.stop() / .fail() / .end / .stopAndRestart()


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
                    throw new RuntimeException("err");
                 //   return RepeatStatus.FINISHED;
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
                    return RepeatStatus.FINISHED;
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

    @Bean
    public Flow flow2_sf(){
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow2_sf");
        flowFlowBuilder.start(step3_sf())
                .next(step4_sf())
                .end();
        return flowFlowBuilder.build();
    }

}
