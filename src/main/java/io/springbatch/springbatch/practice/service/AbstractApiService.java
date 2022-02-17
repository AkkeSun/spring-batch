package io.springbatch.springbatch.practice.service;

import io.springbatch.springbatch.practice.batch.domain.ApiInfo;
import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ApiResponseVo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class AbstractApiService {

    // API 통신을 위한 restTemplate 생성 및 request Parameter 를 설정하는 매소드
    public ApiResponseVo service( List<? extends ApiRequestVo> apiRequestList ){

        // RestTemplate 기본 설정
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        RestTemplate restTemplate = restTemplateBuilder.build();

        // ApiInfo 생성
        ApiInfo apiInfo = ApiInfo.builder().apiRequestList(apiRequestList).build();

        // response Data 리턴
        return doApiService(restTemplate, apiInfo);
    };
    
    // API 통신 후 받아온 데이터를 바탕으로 최종 객채를 생성하는 매소드
    protected abstract ApiResponseVo doApiService(RestTemplate restTemplate, ApiInfo apiInfo);
}
