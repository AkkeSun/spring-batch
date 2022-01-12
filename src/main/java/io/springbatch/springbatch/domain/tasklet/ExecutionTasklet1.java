package io.springbatch.springbatch.domain.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;


@Component
public class ExecutionTasklet1 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("======= ExecutionTasklet1 =======");

        //ExecutionContext 꺼내기
        ExecutionContext jobExecutionContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext(); // 비어있음
        ExecutionContext stepExecutionContext = stepContribution.getStepExecution().getExecutionContext();                  // 비어있음

        // name 꺼내기
        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName(); // helloJob
        String stepName = chunkContext.getStepContext().getStepExecution().getStepName();                                  // step1
        // stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
        // stepContribution.getStepExecution().getStepName();


        // jobExecutionContext 에 jobName 을 Key 로 하는 값을 저장한 적이 없는 경우
        if(jobExecutionContext.get("jobName") == null){
            jobExecutionContext.put("jobName", jobName);
        }

        // stepExecutionContext 에 stepName 을 Key 로 하는 값을 저장한 적이 없는 경우
        if (stepExecutionContext.get("stepName") == null){
            stepExecutionContext.put("stepName", stepName);
        }

        System.out.println("jobName : " + jobExecutionContext.get("jobName"));        // helloJob
        System.out.println("stepName : " + stepExecutionContext.get("stepName"));     // step1

        return RepeatStatus.FINISHED; // 반복안함
    }
}
