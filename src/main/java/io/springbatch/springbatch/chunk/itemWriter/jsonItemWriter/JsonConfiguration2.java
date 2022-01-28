package io.springbatch.springbatch.chunk.itemWriter.jsonItemWriter;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 2차원 데이터(표)로 표현된 유형의 파일을 처리하는 ItemWriter
 */
@Configuration
@RequiredArgsConstructor
public class JsonConfiguration2 {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jsonJob__(){
        return jobBuilderFactory.get("jsonJob__")
                .incrementer(new RunIdIncrementer())
                .start(json_step__())
                .build();
    }


    @Bean
    public Step json_step__() {
        return stepBuilderFactory.get("json_step__")
                .<Customer2, Customer2>chunk(5)
                .reader(listItemReader())
                .writer(jsonItemWriter())
                .build();
    }

    public ItemReader<Customer2> listItemReader(){

        List<Customer2> list = Arrays.asList(new Customer2("sun", 13, "2022"),
                                            new Customer2("exg", 22, "1994"),
                                            new Customer2("noh", 22, "1994"));
        return new ListItemReader<Customer2>(list);
    }


    public ItemWriter<Customer2> jsonItemWriter(){
        return new JsonFileItemWriterBuilder<Customer2>()
                .name("jsonItemWriter")
                .resource(new FileSystemResource("C:\\spring-batch\\src\\main\\resources\\new\\jsonItemWriter.json"))      // 저장 경로
                .append(false) // 오버라이딩 가능 유무
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .build();
    }




}
