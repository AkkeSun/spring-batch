package io.springbatch.springbatch.multiThread.multiThreadStep;

import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CustomItemWriterListener implements ItemWriteListener<Integer> {

    @Override
    public void beforeWrite(List<? extends Integer> list) {

    }

    @Override
    public void afterWrite(List<? extends Integer> items) {
        // 어떤 Thread 를 사용하고 있는지 확인
        String threadName = Thread.currentThread().getName();
        System.err.println("Thread : " + threadName + " || writer item Size : " + items.size());
    }

    @Override
    public void onWriteError(Exception e, List<? extends Integer> list) {

    }
}
