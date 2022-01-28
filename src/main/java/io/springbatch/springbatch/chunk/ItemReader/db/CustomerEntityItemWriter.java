package io.springbatch.springbatch.chunk.ItemReader.db;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerEntityItemWriter implements ItemWriter<CustomerEntity> {

    @Override
    public void write(List<? extends CustomerEntity> list) throws Exception {
        list.forEach(item -> System.out.println(item));
    }
}
