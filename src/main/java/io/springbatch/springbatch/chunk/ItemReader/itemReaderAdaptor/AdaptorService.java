package io.springbatch.springbatch.chunk.ItemReader.itemReaderAdaptor;

import io.springbatch.springbatch.chunk.ItemReader.CustomerEntity;
import io.springbatch.springbatch.chunk.ItemReader.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdaptorService {

    @Autowired
    private CustomerRepository repository;

    private int cnt;
    
    // 하나씩 출력해준다 : 기본적으로 무한반복이다. 이를 피하기 위한 코딩
    public CustomerEntity testMethod(){
        List<CustomerEntity> list = repository.findAll();
        CustomerEntity entity = null;

        if(cnt < list.size()){
            entity = list.get(cnt);
            cnt ++;
        }
        return entity;
    };

}
