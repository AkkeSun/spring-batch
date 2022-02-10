package io.springbatch.springbatch.multiThread.parellel.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

// Multi Thread 처리 시 동시성 이슈를 해결하기위해 동기화 처리
@Component
public class ParallelTasklet2 implements Tasklet {

    private long clsVar = 0;
    private Object lock = new Object();

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
            
        // 동기화 처리 : 한 Thread 가 작업 수행을 마치면 다음 Thread 가 작업을 진행하도록
        synchronized (lock){
            for (int i = 0; i < 100000; i++) {
                clsVar++;
            }
            String stepName = chunkContext.getStepContext().getStepName();
            String threadName = Thread.currentThread().getName();
            System.err.println("Thread : " + threadName + " || stepName : " + stepName);
            System.err.println("classVariable : " + clsVar);
        }
            
        return RepeatStatus.FINISHED;
    }
}
