package io.springbatch.springbatch.practice.batch.chunk.writer;

import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ApiResponseVo;
import io.springbatch.springbatch.practice.service.AbstractApiService;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class ApiItemWriter4 implements ItemWriter<ApiRequestVo> {
    private final AbstractApiService service;

    public ApiItemWriter4(AbstractApiService service) {
        this.service = service;
    }

    @Override
    public void write(List<? extends ApiRequestVo> list) throws Exception {
        ApiResponseVo responseVo = this.service.service(list);
        System.err.println("[WRITER] : STATUS = "+ responseVo.getStatus() + " || MESSAGE = " + responseVo.getMessage());
    }
}
