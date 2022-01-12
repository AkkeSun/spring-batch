package io.springbatch.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing // spring Batch 사용
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
