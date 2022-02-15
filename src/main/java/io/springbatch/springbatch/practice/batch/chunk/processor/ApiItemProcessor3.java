package io.springbatch.springbatch.practice.batch.chunk.processor;

import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessor3 implements ItemProcessor<ProductVo, ApiRequestVo> {

    @Override
    public ApiRequestVo process(ProductVo productVo) throws Exception {
        return ApiRequestVo.builder()
                .id(productVo.getId())
                .productVo(productVo)
                .build();
    }
}
