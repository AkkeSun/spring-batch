package io.springbatch.springbatch.jobAndStep.parameterIncrement;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 커스텀 JobParametersIncrementer : 굳이 사용하지 않을거같애
 */
@Component
public class BatchRunJobParametersIncrement implements JobParametersIncrementer {

    static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Override
    public JobParameters getNext(JobParameters jobParameters) {

        String id = simpleDateFormat.format(new Date());
        return new JobParametersBuilder().addString("run_id", id).toJobParameters();
    }
}
