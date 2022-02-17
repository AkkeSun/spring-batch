package io.springbatch.springbatch.practice.batch.listener;

import io.springbatch.springbatch.practice.batch.domain.ProductVo;

import org.springframework.batch.core.ItemReadListener;

public class ApiReaderListener implements ItemReadListener <ProductVo>{

    @Override
    public void beforeRead() {

    }

    @Override
    public void afterRead(ProductVo productVo) {
        String threadName = Thread.currentThread().getName();
        System.err.println("[LISTENER] : THREAD =" + threadName + " || item = " + productVo);
    }

    @Override
    public void onReadError(Exception e) {

    }
}
