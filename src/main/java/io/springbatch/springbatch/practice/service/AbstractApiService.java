package io.springbatch.springbatch.practice.service;

import io.springbatch.springbatch.practice.batch.domain.ApiInfo;
import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ApiResponseVo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class AbstractApiService {

    public ApiResponseVo service( List<? extends ApiRequestVo> apiRequestList ){

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        */

        ApiInfo apiInfo = ApiInfo.builder().apiRequestList(apiRequestList).build();

        return doApiService(restTemplate, apiInfo);
    };


    protected abstract ApiResponseVo doApiService(RestTemplate restTemplate, ApiInfo apiInfo);

}
