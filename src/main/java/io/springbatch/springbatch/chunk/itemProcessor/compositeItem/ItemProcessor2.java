package io.springbatch.springbatch.chunk.itemProcessor.compositeItem;

import org.springframework.batch.item.ItemProcessor;

public class ItemProcessor2 implements ItemProcessor<String, String> {

    private int cnt = 9;

    @Override
    public String process(String item) throws Exception {

        cnt --;

        return item + cnt;
    }
}
