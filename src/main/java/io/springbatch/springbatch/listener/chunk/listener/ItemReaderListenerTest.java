package io.springbatch.springbatch.listener.chunk.listener;

import org.springframework.batch.core.ItemReadListener;

public class ItemReaderListenerTest implements ItemReadListener <Integer>{

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(Integer integer) {
        System.err.println("  >> [ItemReader] Item : " + integer);
    }

    @Override
    public void onReadError(Exception e) {

    }
}
