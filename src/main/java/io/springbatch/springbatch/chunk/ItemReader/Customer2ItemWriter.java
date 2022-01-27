package io.springbatch.springbatch.chunk.ItemReader;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Customer2ItemWriter implements ItemWriter<Customer2> {

    @Override
    public void write(List<? extends Customer2> list) throws Exception {
        list.forEach(item -> System.out.println(item));
    }
}
