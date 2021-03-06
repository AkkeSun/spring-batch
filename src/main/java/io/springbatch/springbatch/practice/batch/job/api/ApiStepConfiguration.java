package io.springbatch.springbatch.practice.batch.job.api;

import io.springbatch.springbatch.practice.batch.chunk.processor.*;
import io.springbatch.springbatch.practice.batch.chunk.writer.*;
import io.springbatch.springbatch.practice.batch.classifier.ApiProcessorClassifier;
import io.springbatch.springbatch.practice.batch.classifier.ApiWriterClassifier;
import io.springbatch.springbatch.practice.batch.domain.ApiRequestVo;
import io.springbatch.springbatch.practice.batch.domain.ProductVo;
import io.springbatch.springbatch.practice.batch.listener.ApiReaderListener;
import io.springbatch.springbatch.practice.batch.listener.ApiProcessorListener;
import io.springbatch.springbatch.practice.batch.partition.ProductPartitioner;
import io.springbatch.springbatch.practice.service.*;
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
    private final ApiService1 apiService1;
    private final ApiService2 apiService2;
    private final ApiService3 apiService3;
    private final ApiService4 apiService4;
    private final ApiService5 apiService5;
    private int chunkSize = 10;

    @Bean
    public Step apiMasterStep() throws Exception {
        return stepBuilderFactory.get("apiMasterStep")
                .partitioner(apiSlaveStep().getName(), apiPartitioner()) // slaveStep ??????, partitioner ??????
                .step(apiSlaveStep())             // slaveStep ??????
                .gridSize(5)                      // ????????? ????????? ??????
                .taskExecutor(apiTaskExecutor())  // MultiThread ??????
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
                .listener(new ApiReaderListener())
                .processor(apiItemProcessor())
                .listener(new ApiProcessorListener())
                .writer(apiItemWriter())
                .build();
    }



    @Bean
    @StepScope
    //partitioner ?????? ????????? StepExecutionContext ??????????????? ????????????  
    public ItemReader<ProductVo> apiItemReader(@Value("#{StepExecutionContext['productVo']}") ProductVo productVo ) throws Exception {

        // ????????? ?????? 
        MySqlPagingQueryProvider pagingQueryProvider = new MySqlPagingQueryProvider();
        pagingQueryProvider.setSelectClause("id, name, price, type");
        pagingQueryProvider.setFromClause("from product");
        pagingQueryProvider.setWhereClause("where type = :type");

        // ?????? ?????? 
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.DESCENDING);
        pagingQueryProvider.setSortKeys(sortKeys);

        // ??????
        JdbcPagingItemReader<ProductVo> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(chunkSize);
        reader.setQueryProvider(pagingQueryProvider);   
        reader.setRowMapper(new BeanPropertyRowMapper<>(ProductVo.class)); // ????????? ??????

        reader.setParameterValues(
                QueryGenerator.getParameterForQuery("type", productVo.getType())); // ???????????????
        reader.afterPropertiesSet();

        return reader;
    }


    @Bean
    // ClassifierCompositeItemProcessor : ????????? ?????? ?????? ItemProcessor ??? ??????????????? ?????????
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
        // ?????? ????????????
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
    // ClassifierCompositeItemWriter : ????????? ?????? ?????? ItemWriter ??? ??????????????? ?????????
    public ItemWriter apiItemWriter() {

        ClassifierCompositeItemWriter<ApiRequestVo> writer = new ClassifierCompositeItemWriter<>();
        ApiWriterClassifier classifier = new ApiWriterClassifier();
        Map<String, ItemWriter<ApiRequestVo>> writerMap = new HashMap<>();

        writerMap.put("1", new ApiItemWriter1(apiService1));
        writerMap.put("2", new ApiItemWriter2(apiService2));
        writerMap.put("3", new ApiItemWriter3(apiService3));
        writerMap.put("4", new ApiItemWriter4(apiService4));
        writerMap.put("5", new ApiItemWriter5(apiService5));

        classifier.SetProcessorMap(writerMap);
        writer.setClassifier(classifier);


        // ?????? ????????????
        /*
        writer.setClassifier(apiRequestVo -> {
            switch(apiRequestVo.getProductVo().getType()){
                case "1" : return new ApiItemWriter1();
                case "2" : return new ApiItemWriter2();
                case "3" : return new ApiItemWriter3();
                case "4" : return new ApiItemWriter4();
                default  : return new ApiItemWriter5();
            }
        });
        */

        return writer;
    }


    @Bean
    public TaskExecutor apiTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setThreadNamePrefix("API-THREAD");
        return taskExecutor;
    }

}
