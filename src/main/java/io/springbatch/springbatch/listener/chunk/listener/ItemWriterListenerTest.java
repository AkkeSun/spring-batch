package io.springbatch.springbatch.listener.chunk.listener;

import org.springframework.batch.core.ItemWriteListener;
import java.util.List;

public class ItemWriterListenerTest implements ItemWriteListener {


    @Override
    public void beforeWrite(List list) {

    }

    @Override
    public void afterWrite(List list) {
        System.err.println("  >> [ItemWriter] ItemSize : " + list.size());
    }

    @Override
    public void onWriteError(Exception e, List list) {

    }
}
