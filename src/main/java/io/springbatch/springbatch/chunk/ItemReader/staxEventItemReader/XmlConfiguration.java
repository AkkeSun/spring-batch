package io.springbatch.springbatch.chunk.ItemReader.staxEventItemReader;

import io.springbatch.springbatch.chunk.ItemReader.Customer2;
import io.springbatch.springbatch.chunk.ItemReader.Customer2ItemWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * XML 파일을 객채로 파싱
 */
@Configuration
@RequiredArgsConstructor
public class XmlConfiguration {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final Customer2ItemWriter itemWriter;

    @Bean
    public Job staxJob() {
        return jobBuilderFactory.get("staxJob")
                .incrementer(new RunIdIncrementer())
                .start(se_step())
                .build();
    }

    @Bean
    public Step se_step() {
        return stepBuilderFactory.get("se_step")
                .<Customer2, Customer2>chunk(5)
                .reader(XMLItemReader())
                .writer(itemWriter)
                .build();
    }

    private ItemReader<? extends Customer2> XMLItemReader() {
        return new StaxEventItemReaderBuilder<Customer2>()
                .name("xmlItemReader")
                .resource(new ClassPathResource("customer.xml"))
                .addFragmentRootElements("customers") // root fragment
                .unmarshaller(itemUnmarshaller())     // 매핑 셋업
                .build();
    }

    private Unmarshaller itemUnmarshaller() {
        Map<String, Class<?>> aliases = new HashMap<>();

        // 객채
        aliases.put("customer", Customer2.class);
        // 파라미터
        aliases.put("name", String.class);
        aliases.put("age", Integer.class);
        aliases.put("year", String.class);

        XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
        xStreamMarshaller.setAliases(aliases);

        return xStreamMarshaller;
    }

}
