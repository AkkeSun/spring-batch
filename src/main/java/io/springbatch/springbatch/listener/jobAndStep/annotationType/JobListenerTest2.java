package io.springbatch.springbatch.listener.jobAndStep.annotationType;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

public class JobListenerTest2 {

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        System.err.println("================================================================");
        System.err.println("[JOB] " + "\""+ jobName + "\"" + " IS STARTED");

        // jobExecutionContext 설정하기
        jobExecution.getExecutionContext().put("jobData", "jobData1");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();

        long startTime = jobExecution.getStartTime().getTime();
        long endTime = jobExecution.getEndTime().getTime();
        long time = endTime - startTime;
        BatchStatus status = jobExecution.getStatus();
        System.err.println("[JOB] Batch Status : "+ status);
        System.err.println("[JOB] 총 소요시간 : " + time + " ms");
        System.err.println("[JOB] " + "\""+ jobName + "\"" + " IS END");
        System.err.println("================================================================");
    }
}
