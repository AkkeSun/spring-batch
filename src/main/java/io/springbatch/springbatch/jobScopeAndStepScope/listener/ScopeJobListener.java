package io.springbatch.springbatch.jobScopeAndStepScope.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class ScopeJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().put("message2", "msg2");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
