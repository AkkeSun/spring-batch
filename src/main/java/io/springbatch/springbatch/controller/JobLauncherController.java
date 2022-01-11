package io.springbatch.springbatch.controller;

import io.springbatch.springbatch.dto.Member;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class JobLauncherController {

    @Autowired
    private Job job;

    @Autowired
    private BasicBatchConfigurer basicBatchConfigurer;

    @Autowired
    private JobLauncher jobLauncher;

    @PostMapping("/batch")
    public String launcher (@RequestBody Member member) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        String id = member.getId();

        // JobParameters 생성
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", member.getId())
                .addDate("date", new Date())
                .addString("requestDate", "2022-01-11")
                .toJobParameters();

        /*
        - 동기 : JobExecution을 획득하고 배치 처리를 최종 완료한 후에 JobExecution을 반환
        - 비동기 : JobExecution(UnKnown)을 획득하고 바로 JobExecution을 반환 한 후에 배치 처리를 완료 (HTTP요처에 의한 배치에 적합)
         */

        /*
        비동기 처리 시 셋업
        SimpleJobLauncher jobLauncher = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
         */

        jobLauncher.run(job, jobParameters);
        return "batch Completed";
    }
}
