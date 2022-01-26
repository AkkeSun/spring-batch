package io.springbatch.springbatch.chunk.stream;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemStreamProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {

        item = item + " update";
        System.out.println(">>>>>>> ItemProcessor : (DATA : " + item + " )");

        return item;
    }
}
