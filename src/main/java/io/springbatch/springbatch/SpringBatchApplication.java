package io.springbatch.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/*

 @EnableBatchProcessing
 총 4개의 설정 클래스를 실행시키며 스프링 배치를 초기화

 1. BatchAutoConfiguration
    - Job을 수행하는 JobLauncherApplicationRunner 빈을 생성
 2. SimpleBatchConfiguration
    - JobBuilderFactory와 StepBuilderFactory 생성
    - 스프링 배치의 주요 구성요소를 프록시로 생성
 3. BatchConfigureConfiguration
    - BasicBatchConfiguration 생성 : SimpleBatchConfiguration이 생성한 프록시 객채의 실제 객채를 생성
    - JpaBatchConfigurer 생성 : JPA 관련 객채 설정
 4. BatchAutoConfiguration 
    - 사용자 정의 BatchConfigurer 인터페이스 구현

*/
@SpringBootApplication
@EnableBatchProcessing // spring Batch 사용
public class SpringBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
    }

}
