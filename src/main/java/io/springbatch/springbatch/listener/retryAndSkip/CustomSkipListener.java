package io.springbatch.springbatch.listener.retryAndSkip;

import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Integer, Integer> {

    @Override
    // Read 수행중 Skip 발생할 때
    public void onSkipInRead(Throwable throwable) {
        System.err.println("  == [SKIP_READ] msg : " + throwable.getMessage());
    }

    @Override
    // Write 수행중 Skip 발생할 때
    public void onSkipInWrite(Integer integer, Throwable throwable) {
        System.err.println("  == [SKIP_PROCESS] Item : " + integer);
        System.err.println("  == [SKIP_WRITE] msg : " + throwable.getMessage());
    }

    @Override
    // Process 수행중 Skip 발생할 때
    public void onSkipInProcess(Integer integer, Throwable throwable) {
        System.err.println("  >> [SKIP_PROCESS] Item : " + integer);
        System.err.println("  >> [SKIP_PROCESS] msg : " + throwable.getMessage());
    }
}
