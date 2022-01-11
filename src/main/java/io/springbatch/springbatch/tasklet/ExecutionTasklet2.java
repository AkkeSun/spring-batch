package io.springbatch.springbatch.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class ExecutionTasklet2 implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        System.out.println("======= ExecutionTasklet2 =======");

        //ExecutionContext 꺼내기
        ExecutionContext jobExecutionContext = stepContribution.getStepExecution().getJobExecution().getExecutionContext();
        ExecutionContext stepExecutionContext = stepContribution.getStepExecution().getExecutionContext();

        // Step 끼리는 데이터가 공유되지 않기 때문에 비어있다 (jobName 만 출력된다)
        System.out.println("jobName : "+ jobExecutionContext.get("jobName"));    // helloJob
        System.out.println("stepName : "+ stepExecutionContext.get("stepName")); // null
        // chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().get("jobName");
        // chunkContext.getStepContext().getStepExecution().getExecutionContext().get("stepName");


        String stepName = chunkContext.getStepContext().getStepExecution().getStepName(); // step2

        // 오류 발생으로 인해 해당 Step 과 Job 이 Failed 
        // Job 을 다시 실행하는 경우 여기 Step 부터 실행된다
        if (stepExecutionContext.get("stepName") == null){
            stepExecutionContext.put("stepName", stepName);
        //  throw new RuntimeException("step2 was failed");
        }

        return RepeatStatus.FINISHED;
    }
}
