package io.springbatch.springbatch.chunk.itemProcessor.classifierComposite;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;


/**
 * 조건에 따라 다른 ItemProcessor 를 사용한다
 */
@Configuration
@RequiredArgsConstructor
public class ClassifierCompositeItemConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job cfJob_(){
        return jobBuilderFactory.get("cfJob_")
                .incrementer(new RunIdIncrementer())
                .start(cfStep_())
                .build();
    }

    private Step cfStep_() {
        return stepBuilderFactory.get("cfStep_")
                .<ProcessorInfo, ProcessorInfo>chunk(3)
                .reader(reader ())
                .processor(compositeItemProcessor())
                .writer(item -> System.out.println(item))
                .build();
    }

    public ItemReader<ProcessorInfo> reader () {
        ItemReader<ProcessorInfo> itemReader = new ItemReader<ProcessorInfo>() {

            int i = 0;

            @Override
            public ProcessorInfo read() {
                i++;
                ProcessorInfo processorInfo = ProcessorInfo.builder().id(i).build();

                return i>3? null : processorInfo;
            }
        };
        return itemReader;
    }

    private ItemProcessor<? super ProcessorInfo, ProcessorInfo> compositeItemProcessor() {

        ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> itemProcessor =
                new ClassifierCompositeItemProcessor<>();

        // processorInfo.getId()를 key 값으로 갖도록 규칙을 설정
        ProcessorClassifier classifier = new ProcessorClassifier();

        Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();
        processorMap.put(1, new TestItemProcessor1());
        processorMap.put(2, new TestItemProcessor2());
        processorMap.put(3, new TestItemProcessor3());

        classifier.setProcessMap(processorMap);
        itemProcessor.setClassifier(classifier);

        // 이렇게 바로 만들어 줄 수도 있다
        /*
        itemProcessor.setClassifier(processorInfo -> {
            switch (processorInfo.getId()) {
                case 1:
                    return new TestItemProcessor1();
                case 2:
                    return new TestItemProcessor2();
                default:
                    return new TestItemProcessor3();
            }
        });
        */
        return itemProcessor;
    }

}
