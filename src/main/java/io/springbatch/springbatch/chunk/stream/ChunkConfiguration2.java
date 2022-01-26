 package io.springbatch.springbatch.chunk.stream;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

 /**
  * Stream 기반
  */
@Configuration
@RequiredArgsConstructor
public class ChunkConfiguration2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


     @Bean
     public Job chunkJob2_(){
         return jobBuilderFactory.get("chunkJob2_")
          //      .incrementer(new RunIdIncrementer()) // 이걸 주면 새로운 Job 으로 인식되어 처음부터 배치가 돈다
                 .start(chunkStep__())
                 .build();
     }

     @Bean
     public Step chunkStep__(){
         return stepBuilderFactory.get("chunkStep__")
                 .<String, String>chunk(5) // 해당 사이즈마다 Chunk 생성
                 .reader(itemStreamReader())
                 .processor(processor())
                 .writer(itemStreamWriter())
                 .build();
     }

     @Bean
     public ItemStreamReader itemStreamReader(){
        List<String> items = new ArrayList<>(10);
        for(int i =0; i<10; i++){
            items.add(String.valueOf(i));
        }
        return new CustomItemStreamReader(items);
    }

    @Bean
     public ItemStreamWriter itemStreamWriter(){
         return new CustomItemStreamWriter();
    }

     @Bean
     public ItemProcessor processor(){
         return new CustomItemStreamProcessor();
     }

}
