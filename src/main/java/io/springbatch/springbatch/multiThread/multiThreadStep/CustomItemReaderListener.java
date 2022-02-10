package io.springbatch.springbatch.multiThread.multiThreadStep;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import org.springframework.batch.core.ItemReadListener;

public class CustomItemReaderListener implements ItemReadListener<CustomerEntity> {

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(CustomerEntity item){
        // 어떤 Thread 를 사용하고 있는지 확인
        String threadName = Thread.currentThread().getName();
        System.err.println("Thread : " + threadName + " || read item : " +item.getId());
    }

    @Override
    public void onReadError(Exception e) {

    }
}
