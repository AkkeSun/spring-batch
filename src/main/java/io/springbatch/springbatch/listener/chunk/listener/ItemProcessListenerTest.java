package io.springbatch.springbatch.listener.chunk.listener;

import org.springframework.batch.core.ItemProcessListener;

public class ItemProcessListenerTest implements ItemProcessListener<Integer, Integer> {

    @Override
    public void beforeProcess(Integer integer) {
    }

    @Override
    public void afterProcess(Integer beforeItem, Integer afterItem) {
        System.err.println("  >> [ItemProcess] Item : " + beforeItem + " => " + afterItem);
    }

    @Override
    public void onProcessError(Integer integer, Exception e) {
    }
}
