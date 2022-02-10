package io.springbatch.springbatch.multiThread.multiThreadStep;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessorListener implements ItemProcessListener<CustomerEntity, Integer> {
    @Override
    public void beforeProcess(CustomerEntity s) {

    }

    @Override
    public void afterProcess(CustomerEntity inputItem, Integer outputItem) {
        // 어떤 Thread 를 사용하고 있는지 확인
        String threadName = Thread.currentThread().getName();
        System.err.println("Thread : " + threadName + " || processor item : " +outputItem);
    }

    @Override
    public void onProcessError(CustomerEntity s, Exception e) {

    }
}
