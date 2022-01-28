package io.springbatch.springbatch.chunk.ItemReader.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


@Component
public class JpaRunner implements ApplicationRunner {

    @Autowired
    private CustomerRepository repository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
            /*
            CustomerEntity customer1 = CustomerEntity.builder().name("sun").age(32).year("1991").build();
            CustomerEntity customer2 = CustomerEntity.builder().name("exg").age(32).year("1991").build();
            CustomerEntity customer3 = CustomerEntity.builder().name("noh").age(35).year("1988").build();
            CustomerEntity customer4 = CustomerEntity.builder().name("seb").age(35).year("1988").build();
            CustomerEntity customer5 = CustomerEntity.builder().name("sah").age(44).year("1231").build();
            CustomerEntity customer6 = CustomerEntity.builder().name("age").age(20).year("1991").build();
            CustomerEntity customer7 = CustomerEntity.builder().name("sdf").age(25).year("1991").build();
            CustomerEntity customer8 = CustomerEntity.builder().name("dfa").age(41).year("1991").build();
            repository.save(customer1);
            repository.save(customer2);
            repository.save(customer3);
            repository.save(customer4);
            repository.save(customer5);
            repository.save(customer6);
            repository.save(customer7);
            repository.save(customer8);
            */
    }
}
