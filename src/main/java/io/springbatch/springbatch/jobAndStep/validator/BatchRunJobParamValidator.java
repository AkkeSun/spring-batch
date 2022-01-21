package io.springbatch.springbatch.jobAndStep.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

/**
 * 커스텀 JobParametersValidator
 */
@Component
public class BatchRunJobParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {

        if(jobParameters.getString("id") == null)
            throw new JobParametersInvalidException("ID PARAMETERS IS NULL ");

    }
}
