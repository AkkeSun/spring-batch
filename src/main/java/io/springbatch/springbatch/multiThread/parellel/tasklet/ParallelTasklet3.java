package io.springbatch.springbatch.multiThread.parellel.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

// 로컬변수의 사용 : Bean 끼리 변수를 공유하지 않기 때문에 동시성 이슈가 발생하지 않는다
@Component
public class ParallelTasklet3 implements Tasklet {


    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        long localVal = 0;

        for (int i = 0; i < 100000; i++) {
            localVal++;
        }
        String stepName = chunkContext.getStepContext().getStepName();
        String threadName = Thread.currentThread().getName();
        System.err.println("Thread : " + threadName + " || stepName : " + stepName);
        System.err.println("localVariable : " + localVal);

        return RepeatStatus.FINISHED;
    }
}
