package io.springbatch.springbatch.chunk.basic;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;


public class CustomItemReader implements ItemReader<Customer> {

    private List<Customer> list;

    public CustomItemReader(List<Customer> list){
        this.list = list;
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        // list 가 비어있지 않으면 하나씩 읽고 지우기
        if(!list.isEmpty())
            return list.remove(0);

        return null;
    }

}
