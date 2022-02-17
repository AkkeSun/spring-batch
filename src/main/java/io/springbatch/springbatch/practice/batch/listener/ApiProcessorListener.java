package io.springbatch.springbatch.practice.batch.listener;

import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import org.springframework.batch.core.ItemProcessListener;

public class ApiProcessorListener implements ItemProcessListener<ProductVo, ApiRequestVo> {

    @Override
    public void beforeProcess(ProductVo productVo) {

    }

    @Override
    public void afterProcess(ProductVo productVo, ApiRequestVo apiRequestVo) {
        String threadName = Thread.currentThread().getName();
        System.err.println("[PROCESSOR] : THREAD =" + threadName + " || processItem = " + apiRequestVo);
    }

    @Override
    public void onProcessError(ProductVo productVo, Exception e) {

    }
}
