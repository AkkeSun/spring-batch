package io.springbatch.springbatch.jobAndStep;

import io.springbatch.springbatch.jobAndStep.listener.BatchRunJobListener;
import io.springbatch.springbatch.jobAndStep.step.RunTasklet1;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class JobAndStepConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BatchRunJobListener jobListener;
    private final RunTasklet1 runTasklet1;
    private final JobLauncher jobLauncher;


    /**
     * parentJob () ->  step1_job() -> jobStep_run() -> childJob_run() -> step2_job()
     */
    @Bean
    public Job parentJob(){
        return jobBuilderFactory.get("parentJob_js")      // job 이름

                // ================ 처음 실행할 step 설정 ================
                .start(step1_jp())

                // ================ 다음에 실행할 step 설정 ================
                .next(jobStep_js())

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
                //.validator(new DefaultJobParametersValidator(new String[]{"id", "date"}, new String[]{"count"}))
                // .validator(customJobParamValidator)


                // ================ 리스너 등록 ================
                .listener(jobListener)

                // ================ simpleJob 생성 ================
                .build();
    }


    @Bean
    // JobStepBuilder
    public Step jobStep_js() {
        return stepBuilderFactory.get("jobStep_js") // step 이름

                // ================ JobStep 내에서 실행될 Job 설정 ================
                .job(childJob_js())

                // ================ Job 을 실행할 JobLauncher 설정 ================
                .launcher(jobLauncher)

                // ================ Step 의 ExecutionContext 를 Job 이 실행되는데 필요한 JobParameter 로 변환 ================
                .parametersExtractor(jobParametersExtractor())

                // ================ 리스너를 통해 ExecutionContext 에 파라미터 전달 ================
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        stepExecution.getExecutionContext().put("name", "user1");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        return null;
                    }
                })

                // ================ JobStep 생성 ================
                .build();
    }

    // ExecutionContext 의 key 값을 가져오는 함수
    private DefaultJobParametersExtractor jobParametersExtractor () {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }



    @Bean
    public Job childJob_js (){
        return jobBuilderFactory.get("childJob_js")
                .start(step2_jp())
                .build();
    }


    @Bean
    public Step step1_jp(){
        return stepBuilderFactory.get("step1_jp")          //스탭 이름

                // ================ tasklet 설정 ================
                .tasklet(runTasklet1)

                // ================ step 의 성공 유무와 상관없이 항상 Step 을 실행 ================
                // 현재 Step 이 성공하고 다음 Step 이 실패해도 새로운 batch 가 돌 때 실행함
                .allowStartIfComplete(true)

                // ================ step 의 최대 실행횟수 ================
                // .startLimit(1)


                // ================ Step 라이프 사이클 특정 시점에 콜백을 제공받도록 ================
                // .listener(StepExecutionListener)

                // ================ Tasklet Step 생성 ================
                .build();
    }


    @Bean
    public Step step2_jp(){
        return stepBuilderFactory.get("step2_jp")
                .tasklet(((stepContribution, chunkContext) -> RepeatStatus.FINISHED))
                .build();
    }

}
