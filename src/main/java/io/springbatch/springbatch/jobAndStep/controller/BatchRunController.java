package io.springbatch.springbatch.jobAndStep.controller;

import io.springbatch.springbatch.jobAndStep.JobAndStepConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class BatchRunController {

    @Autowired
    private JobAndStepConfiguration batchRunConfiguration;

    @Autowired
    private JobLauncher jobLauncher;

    @PostMapping("/batchRun")
    public String batchRunTest() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        Job batchJob = batchRunConfiguration.parentJob();

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", "id")
                .addDate("date", new Date())
                .toJobParameters();

        jobLauncher.run(batchJob, jobParameters);

        return "batchRun!";
    }

}
