package io.springbatch.springbatch.chunk.itemWriter.xmlItemWriter;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
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
public class StaxConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;



    @Bean
    public Job xmlJob__(){
        return jobBuilderFactory.get("xmlJob__")
                .incrementer(new RunIdIncrementer())
                .start(xml_step__())
                .build();
    }


    @Bean
    public Step xml_step__() {
        return stepBuilderFactory.get("xml_step__")
                .<Customer2, Customer2>chunk(5)
                .reader(listItemReader())
                .writer(xmlItemWriter())
                .build();
    }

    public ItemReader<Customer2> listItemReader(){

        List<Customer2> list = Arrays.asList(new Customer2("sun", 13, "2022"),
                                            new Customer2("exg", 22, "1994"),
                                            new Customer2("noh", 22, "1994"));
        return new ListItemReader<Customer2>(list);
    }


    public ItemWriter<Customer2> xmlItemWriter(){
        return new StaxEventItemWriterBuilder<Customer2>()
                .name("xmlItemWriter")
                .resource(new FileSystemResource("C:\\spring-batch\\src\\main\\resources\\new\\xmlItemWriter.xml"))      // 저장 경로
                .marshaller(marshaller())             // 데이터를 매핑해주는 marshaller 등록
                .rootTagName("customers")             // 조각단위의 루트가 될 이름
                .overwriteOutput(false)               // 오버라이딩 유무
                .build();
    }

    public XStreamMarshaller marshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();
        aliases.put("customer", Customer2.class);
        aliases.put("name", String.class);
        aliases.put("age", Integer.class);
        aliases.put("year", String.class);

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliases);
        return marshaller;
    }

}
