package io.springbatch.springbatch.repeatAndError.faultTolerant.retry.custom;

import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.RetryException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Component
public class RetryItemProcessor2 implements ItemProcessor<String, Customer> {

    @Autowired
    private RetryTemplate retryTemplate;

    private int cnt = 0;

    @Override
    public Customer process(String item) throws Exception {

       Customer customer = retryTemplate.execute(
               new RetryCallback<Customer, RuntimeException>() {
                   @Override
                   public Customer doWithRetry(RetryContext retryContext) throws RuntimeException {
                       if(item.equals("1") || item.equals("2")){
                           cnt ++;
                           System.out.println("Retry Exception! ==> cnt : " + cnt + "// item : " +item);
                           throw new RetryException("Retry Exception!");
                       }
                       return new Customer(item);
                   }
               },
               new RecoveryCallback<Customer>() {
                   @Override
                   public Customer recover(RetryContext retryContext) throws Exception {
                       System.out.println(">>>>> In RecoveryCallback");
                       return new Customer(item);
                   }
               });
       return customer;
    }
}
