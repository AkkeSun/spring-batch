package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
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

    // job 생성
    @Bean
    public Job helloJob(){
        return jobBuilderFactory.get("helloJob")
                .start(helloStep()) // 스탭 실행
                .next(helloStep2()) // 스탭 실행
                .build();
    }

    @Bean
    // Step 생성
    public Step helloStep(){
        return  stepBuilderFactory.get("helloStep1")
                // tasklet 생성 : 기본적으로 무한반복
                .tasklet( new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("==========================");
                        System.out.println(" >> Hello Spring Batch!! ");
                        System.out.println("==========================");
                        return RepeatStatus.FINISHED; // 반복 안함. return null 과 같은 의미
                    }
                })
                .build()
                ;
    }


    @Bean
    public Step helloStep2(){
        return  stepBuilderFactory.get("helloStep2")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("==========================");
                        System.out.println(" >> Step2 was executed ");
                        System.out.println("==========================");
                        return null;
                    }
                })
                .build()
                ;
    }
}

