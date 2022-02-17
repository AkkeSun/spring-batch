package io.springbatch.springbatch.domain.listener;

import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JobExecutionListener 예제
 */
@Component
public class JobRepositoryListener implements JobExecutionListener {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        String jobName = jobExecution.getJobInstance().getJobName();
        JobParameters jobParameters = new JobParametersBuilder().addString("requestDate", "2022-01-11").toJobParameters();

        // JobExecution 의 최근 정보를 가져오는 쿼리. 조회하고자 하는 jobName, jobParameters 를 정확히 입력해주어야한다.
        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

        if(lastJobExecution != null){
            for(StepExecution execution : lastJobExecution.getStepExecutions()){

                String stepName = execution.getStepName();
                BatchStatus status = execution.getStatus();
                ExitStatus exitStatus = execution.getExitStatus();

                System.out.println("===========================");
                System.out.println("StepName : " + stepName);
                System.out.println("status : " + status);
                System.out.println("exitStatus : " + exitStatus);

            }
        }
    }
}
