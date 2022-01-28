package io.springbatch.springbatch.chunk.itemWriter.db.jpa;

import io.springbatch.springbatch.chunk.ItemReader.db.CustomerEntity;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * ModelMapper 를 이용해
 * CustomerEntity 를 CustomerEntityBackup 으로 변경한 후 리턴
 */
@Component
public class JpaItemProcessor implements ItemProcessor<CustomerEntity, CustomerEntityBackup> {

    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public CustomerEntityBackup process(CustomerEntity customerEntity) throws Exception {
        return modelMapper.map(customerEntity, CustomerEntityBackup.class);
    }
}
