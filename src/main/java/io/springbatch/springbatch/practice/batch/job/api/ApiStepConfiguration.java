package io.springbatch.springbatch.practice.batch.job.api;

import io.springbatch.springbatch.chunk.itemProcessor.classifierComposite.ProcessorClassifier;
import io.springbatch.springbatch.practice.batch.chunk.processor.*;
import io.springbatch.springbatch.practice.batch.chunk.writer.*;
import io.springbatch.springbatch.practice.batch.classifier.ApiProcessorClassifier;
import io.springbatch.springbatch.practice.batch.classifier.ApiWriterClassifier;
import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.Product;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.partition.ProductPartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class ApiStepConfiguration {

    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), apiPartitioner()) // slaveStep 이름, partitioner 등록
                .step(apiSlaveStep())             // slaveStep 등록
                .gridSize(5)                      // 파티션 사이즈 설정
                .taskExecutor(apiTaskExecutor())  // MultiThread 설정
                .build();
    }

    
    @Bean
    public Partitioner apiPartitioner() {
        ProductPartitioner partitioner = new ProductPartitioner();
        partitioner.setDataSource(dataSource);
        return partitioner;
    }


    @Bean
    public Step apiSlaveStep() throws Exception {
        return stepBuilderFactory.get("apiSlaveStep")
                .<ProductVo, ProductVo>chunk(chunkSize)
                .reader(apiItemReader(null))
                .processor(apiItemProcessor())
                .writer(apiItemWriter())
                .build();
    }



    @Bean
    @StepScope
    //partitioner 에서 저장된 StepExecutionContext 파라미터로 가져온다  
    public ItemReader<ProductVo> apiItemReader(@Value("#{StepExecutionContext['productVo']}") ProductVo productVo ) throws Exception {

        // 쿼리문 설정 
        MySqlPagingQueryProvider pagingQueryProvider = new MySqlPagingQueryProvider();
        pagingQueryProvider.setSelectClause("id, name, price, type");
        pagingQueryProvider.setFromClause("from product");
        pagingQueryProvider.setWhereClause("where type = :type");

        // 정렬 설정 
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.DESCENDING);
        pagingQueryProvider.setSortKeys(sortKeys);

        // 등록
        JdbcPagingItemReader<ProductVo> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setQueryProvider(pagingQueryProvider);   
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVo.class)); // 매핑할 객채

        reader.setParameterValues(
                QueryGenerator.getParameterForQuery("type", productVo.getType())); // 파라미터값
        reader.afterPropertiesSet();

        return reader;
    }


    @Bean
    // ClassifierCompositeItemProcessor : 조건에 따라 다른 ItemProcessor 가 사용되도록 해준다
    public ItemProcessor apiItemProcessor() {

        ClassifierCompositeItemProcessor<ProductVo, ApiRequestVo> processor =
                new ClassifierCompositeItemProcessor<ProductVo, ApiRequestVo>();

        ApiProcessorClassifier classifier = new ApiProcessorClassifier();
        Map<String, ItemProcessor<ProductVo, ApiRequestVo>> processorMap = new HashMap<>();

        processorMap.put("1", new ApiItemProcessor1());
        processorMap.put("2", new ApiItemProcessor2());
        processorMap.put("3", new ApiItemProcessor3());
        processorMap.put("4", new ApiItemProcessor4());
        processorMap.put("5", new ApiItemProcessor5());

        classifier.SetProcessorMap(processorMap);
        processor.setClassifier(classifier);

        /*
        // 직접 작성하기
        processor.setClassifier(productVo -> {
            switch(productVo.getType()){
                case "1" : return new ApiItemProcessor1();
                case "2" : return new ApiItemProcessor2();
                case "3" : return new ApiItemProcessor3();
                case "4" : return new ApiItemProcessor4();
                default  : return new ApiItemProcessor5();
            }
        });
        */

        return processor;
    }



    @Bean
    // ClassifierCompositeItemWriter : 조건에 따라 다른 ItemWriter 가 사용되도록 해준다
    public ItemWriter apiItemWriter() {

        ClassifierCompositeItemWriter<ApiRequestVo> writer = new ClassifierCompositeItemWriter<>();
        ApiWriterClassifier classifier = new ApiWriterClassifier();
        Map<String, ItemWriter<ApiRequestVo>> writerMap = new HashMap<>();

        writerMap.put("1", new ApiItemWriter1());
        writerMap.put("2", new ApiItemWriter2());
        writerMap.put("3", new ApiItemWriter3());
        writerMap.put("4", new ApiItemWriter4());
        writerMap.put("5", new ApiItemWriter5());

        classifier.SetProcessorMap(writerMap);
        writer.setClassifier(classifier);
        return writer;
    }


    @Bean
    public TaskExecutor apiTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("API-THREAD");
        return null;
    }

}
