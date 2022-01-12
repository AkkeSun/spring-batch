package io.springbatch.springbatch.batchRun;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 커스텀 JobParametersIncrementer
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
