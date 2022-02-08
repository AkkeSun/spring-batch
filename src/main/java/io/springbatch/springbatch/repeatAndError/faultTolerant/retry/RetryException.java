package io.springbatch.springbatch.repeatAndError.faultTolerant.retry;

public class RetryException extends RuntimeException{
    public RetryException(String message) {
        super(message);
    }
}
