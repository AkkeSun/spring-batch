package io.springbatch.springbatch.repeatAndError.faultTolerant.skip;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        for(String item : items){
            if(item.equals("1")){
                System.out.println("ItemWriter Exception!");
                throw new SkipException("ItemWriter Exception!");
            }
            else
                System.out.println("ItemWriter -> " + item);
        }
    }
}
