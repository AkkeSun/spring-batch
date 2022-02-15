package io.springbatch.springbatch.practice.batch.job.file;

import io.springbatch.springbatch.practice.batch.chunk.processor.FileItemProcessor;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class FileJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final int chunkSize = 10;

    @Bean
    public Job fileJob__(){
        return jobBuilderFactory.get("fileJob__")
                .start(fileStep__())
                .build();
    }
    @Bean
    public Step fileStep__() {
        return stepBuilderFactory.get("fileStep__")
                .<ProductVo, Product>chunk(chunkSize)
                .reader(fileItemReader(null))
                .processor(fileItemProcessor())
                .writer(fileItemWriter())
                .build();
    }


    @Bean
    @JobScope
    public FlatFileItemReader<ProductVo> fileItemReader(@Value("#{jobParameters['requestDate']}") String requestDate){
        return new FlatFileItemReaderBuilder<ProductVo>()
                .name("fileReader")
                .resource(new ClassPathResource("project_" + requestDate + ".txt"))
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())
                .targetType(ProductVo.class)
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("id", "name", "price", "type")
                .build();
    }

    @Bean
    public ItemProcessor<ProductVo, Product> fileItemProcessor() {
        return new FileItemProcessor();
    }

    @Bean
    public ItemWriter<Product> fileItemWriter() {
        return new JpaItemWriterBuilder<Product>()
                .entityManagerFactory(entityManagerFactory)
                .usePersist(true)
                .build();
    }

}



