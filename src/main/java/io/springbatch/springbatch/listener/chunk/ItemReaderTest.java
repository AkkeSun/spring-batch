package io.springbatch.springbatch.listener.chunk;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

@Component
public class ItemReaderTest implements ItemReader<Integer> {
    int i = 0;
    @Override
    public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        i++;
        return i>15 ? null : i;
    }
}
