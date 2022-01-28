package io.springbatch.springbatch.chunk.itemProcessor.CompositeItem;

import org.springframework.batch.item.ItemProcessor;

public class ItemProcessor1 implements ItemProcessor<String, String> {

    private int cnt;

    @Override
    public String process(String item) throws Exception {
        cnt++;
        return (item + cnt).toUpperCase();
    }
}
