package io.springbatch.springbatch.listener.chunk;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemWriterTest implements ItemWriter<Integer> {
    @Override
    public void write(List<? extends Integer> list) throws Exception {
    }
}
