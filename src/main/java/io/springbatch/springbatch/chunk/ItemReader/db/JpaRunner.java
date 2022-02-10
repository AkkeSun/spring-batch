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
        CustomerEntity customer1 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer2 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer3 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer4 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer5 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer6 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer7 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer8 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer9 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer10 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer11 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer12 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer13 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer14 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer15 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer16 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer17 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer18 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer19 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer20 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer21 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer22 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer23 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer24 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer25 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer26 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer27 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer28 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer29 = CustomerEntity.builder().name("test").age(32).year("1991").build();
        CustomerEntity customer30 = CustomerEntity.builder().name("test").age(32).year("1991").build();

        repository.save(customer1);
        repository.save(customer2);
        repository.save(customer3);
        repository.save(customer4);
        repository.save(customer5);
        repository.save(customer6);
        repository.save(customer7);
        repository.save(customer8);
        repository.save(customer9);
        repository.save(customer10);
        repository.save(customer11);
        repository.save(customer12);
        repository.save(customer13);
        repository.save(customer14);
        repository.save(customer15);
        repository.save(customer16);
        repository.save(customer17);
        repository.save(customer18);
        repository.save(customer19);
        repository.save(customer20);
        repository.save(customer21);
        repository.save(customer22);
        repository.save(customer23);
        repository.save(customer24);
        repository.save(customer25);
        repository.save(customer26);
        repository.save(customer27);
        repository.save(customer28);
        repository.save(customer29);
        repository.save(customer30);
*/

    }
}
