package io.springbatch.springbatch.practice.batch.chunk.writer;

import io.springbatch.springbatch.practice.batch.domain.ApiInfo;
import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ApiResponseVo;
import io.springbatch.springbatch.practice.service.AbstractApiService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApiItemWriter1 implements ItemWriter<ApiRequestVo> {


    private final AbstractApiService service;

    public ApiItemWriter1(AbstractApiService service) {
        this.service = service;
    }

    @Override
    public void write(List<? extends ApiRequestVo> list) throws Exception {

        /*
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();
        ApiInfo apiInfo = ApiInfo.builder().apiRequestList(list).build();

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/product/1", apiInfo, String.class);
        int statusCodeValue = responseEntity.getStatusCodeValue();
        ApiResponseVo apiResponseVo = ApiResponseVo.builder().status(statusCodeValue).message(responseEntity.getBody()).build();
        */

        // 위의 코드 리펙토링
        ApiResponseVo responseVo = this.service.service(list);
        System.err.println("[WRITER] : STATUS = "+ responseVo.getStatus() + " || MESSAGE = " + responseVo.getMessage());
    }
}
