package io.springbatch.springbatch.chunk.stream;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

import java.util.List;

public class CustomItemStreamWriter implements ItemStreamWriter<String> {

    @Override
    public void write(List<? extends String> list) throws Exception {
        System.out.println("ItemStreamWriter : WRITE");
        list.forEach(item -> System.out.println(item));
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("ItemStreamWriter : OPEN");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        System.out.println("ItemStreamWriter : UPDATE");

    }

    @Override
    public void close() throws ItemStreamException {
        System.out.println("ItemStreamWriter : CLOSE");
    }


}
