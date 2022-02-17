package io.springbatch.springbatch.practice.service;

import io.springbatch.springbatch.practice.batch.domain.ApiInfo;
import io.springbatch.springbatch.practice.batch.domain.ApiResponseVo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService3 extends AbstractApiService{


    @Override
    protected ApiResponseVo doApiService(RestTemplate restTemplate, ApiInfo apiInfo) {

        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/product/3", apiInfo, String.class);
        int statusCondeValue = responseEntity.getStatusCodeValue();
        ApiResponseVo apiResponseVo = ApiResponseVo.builder().status(statusCondeValue).message(responseEntity.getBody()).build();
        return apiResponseVo;
    }


}
