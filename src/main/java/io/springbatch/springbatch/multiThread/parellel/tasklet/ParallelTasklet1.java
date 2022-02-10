package io.springbatch.springbatch.multiThread.parellel.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

// Multi Thread 처리 시 동시성의 이슈가 발생하는 Tasklet
@Component
public class ParallelTasklet1 implements Tasklet {

    private long clsVar = 0;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

            for (int i = 0; i < 100000; i++) {
                clsVar++;
            }
            String stepName = chunkContext.getStepContext().getStepName();
            String threadName = Thread.currentThread().getName();
            System.err.println("Thread : " + threadName + " || stepName : " + stepName);
            System.err.println("classVariable : " + clsVar);

        return RepeatStatus.FINISHED;
    }
}
