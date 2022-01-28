package io.springbatch.springbatch.chunk.itemWriter.flatFileItemWriter;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.List;


/**
 * 2차원 데이터(표)로 표현된 유형의 파일을 처리하는 ItemWriter
 */
@Configuration
@RequiredArgsConstructor
public class FlatFileConfiguration2 {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;



    @Bean
    public Job flatFileJob2(){
        return jobBuilderFactory.get("flatFileJob2")
                .incrementer(new RunIdIncrementer())
                .start(fp_step1__())
                .next(fp_step2__())
                .build();
    }

    @Bean
    public Step fp_step1__() {
        return stepBuilderFactory.get("fp_step1__")
                .<Customer2, Customer2>chunk(5)
                .reader(listItemReader())
                .writer(flatFileItemWriter1())
                .build();
    }

    @Bean
    public Step fp_step2__() {
        return stepBuilderFactory.get("fp_step2__")
                .<Customer2, Customer2>chunk(5)
                .reader(listItemReader())
                .writer(flatFileItemWriter2())
                .build();
    }

    public ItemReader<Customer2> listItemReader(){

        List<Customer2> list = Arrays.asList(new Customer2("sun", 13, "2022"),
                                            new Customer2("exg", 22, "1994"),
                                            new Customer2("noh", 22, "1994"));
        return new ListItemReader<Customer2>(list);
    }


    public ItemWriter<Customer2> flatFileItemWriter1(){
        return new FlatFileItemWriterBuilder<Customer2>()
                .name("flatFilterItemWriter1")
                .resource(new FileSystemResource("C:\\spring-batch\\src\\main\\resources\\new\\flatFileItemWriter1.txt")) // 저장경로
                .delimited()                  // 구분자 기준으로 라인을 토큰화
                .delimiter("|")               // 구분자 등록
                .names("name", "age", "year") // 매핑할 필드명 지정
                .shouldDeleteIfExists(true)   // 파일이 이미 존재한다면 삭제할 것인지 여부
                .shouldDeleteIfEmpty(true)    // 파일의 내용이 비어있다면 삭제할것인지 여부
                .append(false)                // 대상 파일이 존재하는 경우 데이터를 계속 추가할것인지 여부
                .build();
    }

    public ItemWriter<Customer2> flatFileItemWriter2(){
        return new FlatFileItemWriterBuilder<Customer2>()
                .name("flatFilterItemWriter1")
                .resource(new FileSystemResource("C:\\spring-batch\\src\\main\\resources\\new\\flatFileItemWriter2.txt")) // 저장경로
                .formatted()                  // 고정길이를 기준으로 파일을 작성
                .format("%-3s%-2d%-4s")       // 고정길이 입력
                .names("name", "age", "year") // 매핑할 필드명 지정
                .shouldDeleteIfExists(true)   // 파일이 이미 존재한다면 삭제할 것인지 여부
                .shouldDeleteIfEmpty(true)    // 파일의 내용이 비어있다면 삭제할것인지 여부
                .append(false)                // 대상 파일이 존재하는 경우 데이터를 계속 추가할것인지 여부
                .build();
    }


}
