package io.springbatch.springbatch.jobAndFlow;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * JobExecutionDecider 를 활용한 조건적인 흐름 제어
 */
public class CustomDecider implements JobExecutionDecider {

    private int count = 1;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

        count ++ ;
        if(count % 2 == 0 )
            return new FlowExecutionStatus("EVEN");
        else
            return new FlowExecutionStatus("ODD");
    }

}
