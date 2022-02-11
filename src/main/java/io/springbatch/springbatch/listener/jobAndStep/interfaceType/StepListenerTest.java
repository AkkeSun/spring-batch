package io.springbatch.springbatch.listener.jobAndStep.interfaceType;

import org.springframework.batch.core.*;

public class StepListenerTest implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        System.err.println(">> [STEP] " + "\""+ stepName + "\"" + " IS STARTED");

        // stepExecutionContext 보내기
        stepExecution.getExecutionContext().put("stepData", "data1");

        // jobExecutionContext 꺼내기
        String jobData  = (String)stepExecution.getJobExecution().getExecutionContext().get("jobData");
        System.err.println(">> [STEP] JobData : " + jobData);

    }


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();
        System.err.println(">> [STEP] STATUS : " + exitCode);
        return ExitStatus.COMPLETED;
    }

}
