package io.springbatch.springbatch.batchRun;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class BatchRunConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BatchRunJobListener jobListener;
    private final BatchRunJobParamValidator customJobParamValidator;
    private final BatchRunJobParametersIncrement customRunIncrement;




    @Bean
    // Simple Job
    public Job batchJob(){
        return jobBuilderFactory.get("batchJob")      // job 이름

                // ================ 처음 실행할 step 설정 ================
                .start(batchRunStep1())


                // ================ 다음에 실행할 step 설정 ================
                .next(batchRunStep2())


                // ================ JobParameters 의 값을 자동 증가 ================
                // 다른 컬럼이 바뀌지 않아도 실행가능하도록 셋업
                .incrementer(new RunIdIncrementer())
                //.incrementer(customRunIncrement)



                // ================ Job 이 실패했을 때 재시작 가능 여부 설정 ================
                // 기본값은 true. 위의 기능을 선언하면 false
                // .preventRestart()



                // ================ JobParameters 검증 ================
                // param1 : 필수 파라미터 (없는 경우 오류 발생) / param2 : 옵션 파라미터 (없어도 오류 발생하지 않음)
                // 등록된 파라미터 외의 파라미터는 입력할 수 없음
                .validator(new DefaultJobParametersValidator(new String[]{"id", "date"}, new String[]{"count"}))
                // .validator(customJobParamValidator)



                // ================ 리스너 등록 ================
                .listener(jobListener)


                // ================ simpleJob 생성 ================
                .build();

    }



    @Bean
    // TaskletStepBuilder
    public Step batchRunStep1(){
        return stepBuilderFactory.get("batchRunStep1")          //스탭 이름
                .tasklet((stepContribution, chunkContext) -> {
                    System.out.println("===== Tasklet Step =====");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    // SimpleStepBuilder
    public Step batchRunStep2() {
        return stepBuilderFactory.get("batchRunStep2")
                .<String, String>chunk(3)
                .reader(new ItemReader<String>() {
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String s) throws Exception {
                        return null;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> list) throws Exception {

                    }
                })
                .build();
    }

    @Bean
    // PartitionStepBuilder
    public Step batchRunStep3() {
        return stepBuilderFactory.get("batchRunStep3")
                .partitioner(batchRunStep1())
                .gridSize(2)
                .build();
    }

    @Bean
    // JobStepBuilder
    public Step batchRunStep4() {
        return stepBuilderFactory.get("batchRunStep4")
                .job(batchJob())
                .build();
    }

    @Bean
    // FlowStepBuilder
    public Step batchRunStep5() {
        return stepBuilderFactory.get("batchRunStep5")
                .flow(flow1())
                .build();
    }




    @Bean
    // Flow Job 생성
    public Job batchJob2(){
        return jobBuilderFactory.get("batchJob2")
                .start(flow1())  // start 는 flow 객채로
                .next(step7())
                .end()           // 필수
                .build();
    }


    @Bean
    // flow 객채 생성
    public Flow flow1(){
        FlowBuilder<Flow> flowFlowBuilder = new FlowBuilder<>("flow1");
        flowFlowBuilder.start(step5())
                        .next(step6())
                        .end();         // 필수
        return flowFlowBuilder.build();
    }







}
