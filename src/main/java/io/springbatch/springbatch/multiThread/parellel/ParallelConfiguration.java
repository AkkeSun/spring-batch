package io.springbatch.springbatch.multiThread.parellel;

import io.springbatch.springbatch.multiThread.asyncItemProcessor.StopWatchJobListener;
import io.springbatch.springbatch.multiThread.parellel.tasklet.ParallelTasklet1;
import io.springbatch.springbatch.multiThread.parellel.tasklet.ParallelTasklet2;
import io.springbatch.springbatch.multiThread.parellel.tasklet.ParallelTasklet3;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


/**
 * 여러 개의 Flow 혹은 Step 을 병렬적으로 실행하는 구조
 *
 * [ Bean 과 Thread 의 이해 ]
 * 1. Tasklet 은 Bean(singleTon) 이기 때문에 클래스변수를 공유한다.
 * 2. Multi Thread 를 구현하는 경우 클래스변수가 각각의 Thread 에서 공유되는 동시성의 이슈가 발생한다.
 * 3. 이를 해결하기 위해서 클래스 변수를 처리하는 부분에 한 Thread 가 작업을 종료했을 때 다음 Thread 가 작업을 수행하도록 동기화 처리를 해주어야한다 (synchronized)
 * 4. 사용하는 변수가 클래스 변수가 아닌 로컬변수라면, 변수값을 공유하지 않기 때문에 동시성 문제가 발생하지 않는다.
 */
@Configuration
@RequiredArgsConstructor
public class ParallelConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ParallelTasklet1 parallelTasklet1;
    private final ParallelTasklet2 parallelTasklet2;
    private final ParallelTasklet3 parallelTasklet3;


    /**
     [ Test1. 절차지향적인 실행 _ 클래스 변수 사용 ]
     클래스 변수를 공유하지만 동시성 이슈가 발생하지 않는다
     
     Thread : main || stepName : pStep1
     classVariable : 100000
     Thread : main || stepName : pStep2
     classVariable : 200000
     Thread : main || stepName : pStep3
     classVariable : 300000
     Thread : main || stepName : pStep4
     classVariable : 400000
     =====================================
      총 소요시간 : 157
     =====================================
     */
    @Bean
    public Job ParallelJobTest1(){
        return jobBuilderFactory.get("ParallelJobTest1")
                .incrementer(new RunIdIncrementer())
                .start(parallelFlow1())
                .next(parallelFlow2())
                .next(parallelFlow3())
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }



    /**
     [ Test2. Multi Thread 실행 _ 클래스 변수 사용 ]
     클래스 변수를 공유하고 동시성 이슈가 발생한다
     
     Thread : parallel-thread1 || stepName : pStep2
     Thread : parallel-thread2 || stepName : pStep1
     classVariable : 118187
     classVariable : 118187
     Thread : parallel-thread1 || stepName : pStep3
     classVariable : 218187
     Thread : main || stepName : pStep4
     classVariable : 318187
     =====================================
     총 소요시간 : 125
     =====================================
     */
    @Bean
    public Job ParallelJobTest2(){
        return jobBuilderFactory.get("ParallelJobTest2")
                .incrementer(new RunIdIncrementer())

                // parallelFlow1(), parallelFlow2() 가 병렬로 실행된다
                .start(parallelFlow1())
                .split(parallelExecutor()).add(parallelFlow2())// add()에 여러 Flow 입력 가능

                // 병렬 실행이 종료되면 Main Thread 에서 parallelFlow3()가 실행된다
                .next(parallelFlow3())
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }




    /**
     [ Test3. Multi Thread 실행 _ 클래스 변수 사용 ]
     클래스 변수를 공유하고 동기화 처리를 통해 동시성 이슈를 해결하였다

     Thread : parallel-thread1 || stepName : pStep5
     classVariable : 100000
     Thread : parallel-thread2 || stepName : pStep4
     classVariable : 200000
     Thread : parallel-thread1 || stepName : pStep6
     classVariable : 300000
     Thread : main || stepName : pStep7
     classVariable : 400000
     =====================================
     총 소요시간 : 140
     =====================================
     */
    @Bean
    public Job ParallelJobTest3(){
        return jobBuilderFactory.get("ParallelJobTest3")
                .incrementer(new RunIdIncrementer())
                .start(parallelFlow4())
                .split(parallelExecutor()).add(parallelFlow5())
                .next(parallelFlow6())
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }



    /**
     [ Test4. Multi Thread 실행 _ 로컬 변수 사용 ]
     로컬 변수를 사용하기 때문에 Bean 끼리 변수를 공유하지 않아서 동시성 이슈가 발생하지 않는다

     Thread : parallel-thread2 || stepName : pStep8
     Thread : parallel-thread1 || stepName : pStep9
     localVariable : 100000
     localVariable : 100000
     Thread : parallel-thread1 || stepName : pStep10
     localVariable : 100000
     Thread : main || stepName : pStep11
     localVariable : 100000
     =====================================
     총 소요시간 : 125
     =====================================
     */
    @Bean
    public Job ParallelJobTest4(){
        return jobBuilderFactory.get("ParallelJobTest4")
                .incrementer(new RunIdIncrementer())
                .start(parallelFlow7())
                .split(parallelExecutor()).add(parallelFlow8())
                .next(parallelFlow9())
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }






    //==============test1,2 용 flow =====================
    @Bean
    public Flow parallelFlow1() {
        TaskletStep pStep1 = stepBuilderFactory.get("pStep1").tasklet(parallelTasklet1).build();
        return new FlowBuilder<Flow>("pFlow1")
                .start(pStep1).build();
    }
    @Bean
    public Flow parallelFlow2() {
        TaskletStep pStep2 = stepBuilderFactory.get("pStep2").tasklet(parallelTasklet1).build();
        TaskletStep pStep3 = stepBuilderFactory.get("pStep3").tasklet(parallelTasklet1).build();
        return new FlowBuilder<Flow>("pFlow2")
                .start(pStep2).next(pStep3).build();
    }
    @Bean
    public Flow parallelFlow3() {
        TaskletStep pStep3 = stepBuilderFactory.get("pStep4").tasklet(parallelTasklet1).build();
        return new FlowBuilder<Flow>("pFlow3")
                .start(pStep3).build();
    }

    //==============test3 용 flow =====================
    @Bean
    public Flow parallelFlow4() {
        TaskletStep pStep4 = stepBuilderFactory.get("pStep4").tasklet(parallelTasklet2).build();
        return new FlowBuilder<Flow>("pFlow4")
                .start(pStep4).build();
    }
    @Bean
    public Flow parallelFlow5() {
        TaskletStep pStep5 = stepBuilderFactory.get("pStep5").tasklet(parallelTasklet2).build();
        TaskletStep pStep6 = stepBuilderFactory.get("pStep6").tasklet(parallelTasklet2).build();
        return new FlowBuilder<Flow>("pFlow5")
                .start(pStep5).next(pStep6).build();
    }
    @Bean
    public Flow parallelFlow6() {
        TaskletStep pStep7 = stepBuilderFactory.get("pStep7").tasklet(parallelTasklet2).build();
        return new FlowBuilder<Flow>("pFlow6")
                .start(pStep7).build();
    }

    
    //==============test4 용 flow =====================
    @Bean
    public Flow parallelFlow7() {
        TaskletStep pStep8 = stepBuilderFactory.get("pStep8").tasklet(parallelTasklet3).build();
        return new FlowBuilder<Flow>("pFlow7")
                .start(pStep8).build();
    }
    @Bean
    public Flow parallelFlow8() {
        TaskletStep pStep9 = stepBuilderFactory.get("pStep9").tasklet(parallelTasklet3).build();
        TaskletStep pStep10 = stepBuilderFactory.get("pStep10").tasklet(parallelTasklet3).build();
        return new FlowBuilder<Flow>("pFlow8")
                .start(pStep9).next(pStep10).build();
    }
    @Bean
    public Flow parallelFlow9() {
        TaskletStep pStep11 = stepBuilderFactory.get("pStep11").tasklet(parallelTasklet3).build();
        return new FlowBuilder<Flow>("pFlow9")
                .start(pStep11).build();
    }
    


    @Bean
    public TaskExecutor parallelExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("parallel-thread");
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        return taskExecutor;
    }

}
