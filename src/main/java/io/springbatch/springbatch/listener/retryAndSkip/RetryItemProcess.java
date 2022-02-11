package io.springbatch.springbatch.listener.retryAndSkip;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class RetryItemProcess implements ItemProcessor<Integer, Integer> {

    @Override
    public Integer process(Integer integer) throws Exception {
        if(integer == 3 || integer == 5)
            throw new RuntimeException("Item is : " + integer);
        return integer + 100;
    }
}
