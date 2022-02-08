package io.springbatch.springbatch.repeatAndError.faultTolerant.retry.basic;

import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.RetryException;
import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {

        if(item.equals("2") || item.equals("3")){
            throw new RetryException("Process Failed --> " + item);
        }
        return item;
    }
}
