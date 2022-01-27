package io.springbatch.springbatch.chunk.ItemReader.flatFileItemReader;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;


/**
 * 2차원 데이터(표)로 표현된 유형의 파일을 처리하는 ItemReader
 */
@Configuration
@RequiredArgsConstructor
public class FlatFilterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Customer2ItemWriter itemWriter;

    @Bean
    public Job flatFilterJob(){
        return jobBuilderFactory.get("flatFilterJob")
                .incrementer(new RunIdIncrementer())
                .start(fp_step1())
                .next(fp_step2())
                .build();
    }

    @Bean
    public Step fp_step1() {
        return stepBuilderFactory.get("fp_step1")
                .<Customer2, Customer2>chunk(5)
                .reader(flatFileItemReader1())
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Step fp_step2() {
        return stepBuilderFactory.get("fp_step2")
                .<Customer2, Customer2>chunk(5)
                .reader(flatFileItemReader2())
                .writer(itemWriter)
                .build();
    }


    public FlatFileItemReader flatFileItemReader1(){
        return new FlatFileItemReaderBuilder<Customer2>()
                .name("DelimitedLineTokenizer") // 이름 설정
                .resource(new ClassPathResource("customer.txt")) // 매핑할 리소스
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>()) // fieldSetMapper 입력
                .strict(true)  // 토큰화 검증을 적용할것인지 여부 (기본값 true)
                .targetType(Customer2.class)   // 타겟타입 설정
                .linesToSkip(1)                // 스킵할 라인 설정
                .delimited()                   // 구분자 기준으로 라인을 토큰화
                .delimiter(",")                // 구분자 등록
                .names("name", "age", "year")  // 객채 필드명으로 매핑하도록 지원
                .build();
    }

    public FlatFileItemReader flatFileItemReader2(){
        return new FlatFileItemReaderBuilder<Customer2>()
                .name("FixedLengthTokenizer")
                .resource(new FileSystemResource("C:\\spring-batch\\src\\main\\resources\\customer2.txt"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .strict(true)
                .targetType(Customer2.class)
                .linesToSkip(1)
                .fixedLength()                // 고정길이 기준으로 라인을 토큰화
                .addColumns(new Range(1,5))   // 고정길이 범위 설정
                .addColumns(new Range(6,7))
                .addColumns(new Range(8))
                .names("name", "age", "year")
                .build();
    }
}
