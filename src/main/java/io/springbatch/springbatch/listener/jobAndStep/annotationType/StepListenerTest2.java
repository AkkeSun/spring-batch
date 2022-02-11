package io.springbatch.springbatch.listener.jobAndStep.annotationType;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

public class StepListenerTest2 implements StepExecutionListener {

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        System.err.println(">> [STEP] " + "\""+ stepName + "\"" + " IS STARTED");

        // stepExecutionContext 보내기
        stepExecution.getExecutionContext().put("stepData", "data1");

        // jobExecutionContext 꺼내기
        String jobData  = (String)stepExecution.getJobExecution().getExecutionContext().get("jobData");
        System.err.println(">> [STEP] JobData : " + jobData);

    }


    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        BatchStatus batchStatus = stepExecution.getStatus();
        System.err.println(">> [STEP] STEP STATUS : " + exitCode);
        return ExitStatus.COMPLETED;
    }

}
