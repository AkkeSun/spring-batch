package io.springbatch.springbatch.repeatAndError.faultTolerant.retry.custom;

import io.springbatch.springbatch.repeatAndError.faultTolerant.retry.RetryException;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;


/**
 * [ RetryTemplate ]
 * RetryCallback : 예외 발생시 지정한 횟수만큼 데이터 처리를 재시도하는 함수.
 * DefaultRetryState : 재시도 횟수가 초과되고 RetryState 가  true 라면 chunk 맨 앞단으로 가서 배치를 재시동한다. (결국 예외 발생한다)
 * RecoveryCallback : RetryStatus 가 null 이거나 false 라면 실행되는 함수.
 *
 * RetryTemplate 은 디폴트 값으로 RetryStatus = null 로 셋팅 되어있다.
 * 만약 RecoveryCallback 을 파라미터 값으로 받지 않거나, rollbackClassifier 값을 false 로 설정한다면
 * 재시도 횟수 초과 후 예외발생으로 배치가 종료되지 않고 RecoveryCallback 을 수행됨으로 새로운 비지니스 로직을 짤 수 있다.
 */
@Component
public class RetryItemProcessor2 implements ItemProcessor<String, Customer> {

    @Autowired
    private RetryTemplate retryTemplate;

    private int cnt = 0;

    @Override
    public Customer process(String item) throws Exception {

        // 예외 종류에 따라 값을 선택하는 구현체
        Classifier<Throwable , Boolean> rollbackClassifier = new BinaryExceptionClassifier(true);

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
                       System.out.println(">>>>> In RecoveryCallback => item : " + item);
                       return null;
                   //    return new Customer(item);
                   }
                },
                new DefaultRetryState(item, rollbackClassifier));
       return customer;
    }
}
