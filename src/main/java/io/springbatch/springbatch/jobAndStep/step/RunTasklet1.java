package io.springbatch.springbatch.jobAndStep.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * TASK 기반
 */
@Component
public class RunTasklet1 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        String jobName = stepContribution.getStepExecution().getJobExecution().getJobInstance().getJobName();
        String stepName = stepContribution.getStepExecution().getStepName();

        System.out.println("HELLO Run Tasklet");
        System.out.println("jobName : " + jobName);
        System.out.println("stepName : " + stepName);

        return RepeatStatus.FINISHED; // 반복안함
    }
}
