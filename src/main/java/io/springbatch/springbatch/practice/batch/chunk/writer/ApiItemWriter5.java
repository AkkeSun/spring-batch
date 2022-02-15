package io.springbatch.springbatch.practice.batch.chunk.writer;

import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter5 implements ItemWriter<ApiRequestVo> {
    
    @Override
    public void write(List<? extends ApiRequestVo> list) throws Exception {

    }
}
