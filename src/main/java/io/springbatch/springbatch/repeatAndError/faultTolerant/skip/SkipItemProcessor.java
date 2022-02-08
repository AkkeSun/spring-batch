package io.springbatch.springbatch.repeatAndError.faultTolerant.skip;

import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {

        if(item.equals("2") || item.equals("3")){
            System.out.println("Process Failed --> " + item);
            throw new SkipException("Process Failed --> " + item);
        }
        else
            System.out.println("ItemProcessor : "+item);
        return item;
    }
}
