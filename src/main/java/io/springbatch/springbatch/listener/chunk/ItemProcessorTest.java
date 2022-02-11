package io.springbatch.springbatch.listener.chunk;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ItemProcessorTest implements ItemProcessor<Integer, Integer> {

    @Override
    public Integer process(Integer integer) throws Exception {
        return integer + 100;
    }
}
