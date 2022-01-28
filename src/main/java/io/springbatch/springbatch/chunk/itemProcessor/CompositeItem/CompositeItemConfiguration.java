package io.springbatch.springbatch.chunk.itemProcessor.CompositeItem;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * CompositeItemProcessor Example : 순차적으로 연결
 */
@Configuration
@RequiredArgsConstructor
public class CompositeItemConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job cpJob_(){
        return jobBuilderFactory.get("cpJob_")
                .incrementer(new RunIdIncrementer())
                .start(cpStep_())
                .build();
    }

    private Step cpStep_() {
        return stepBuilderFactory.get("cpStep_")
                .<String, String>chunk(3)
                .reader(new ListItemReader<>(Arrays.asList("item", "item", "item", "item", "item")))
                .processor(compositItemProcessor())
                .writer(item -> System.out.println(item))
                .build();
    }

    private ItemProcessor<? super String, String> compositItemProcessor() {

        List itemProcessor = new ArrayList();
        itemProcessor.add(new ItemProcessor1());
        itemProcessor.add(new ItemProcessor2());

        return new CompositeItemProcessorBuilder<String, String>()
                .delegates(itemProcessor)
                .build();
    }


}
