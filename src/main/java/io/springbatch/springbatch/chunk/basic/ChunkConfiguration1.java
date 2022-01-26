 package io.springbatch.springbatch.chunk.basic;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

 /**
  * 기본적인 Chunk Process
  */
@Configuration
@RequiredArgsConstructor
public class ChunkConfiguration1 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job chunkJob_(){
        return jobBuilderFactory.get("chunkJob_")
                .incrementer(new RunIdIncrementer())
                .start(chunkStep_())
                .next(chunkStep2_())
                .build();
    }


    @Bean
    public Step chunkStep_(){
        return stepBuilderFactory.get("chunkStep_")
                // <I, O> chunk(number) ->  commit interval_ 숫자만큼 데이터 처리후 commit
                // <I, O> chunk(CompletionPolicy) ->  Chunk 프로세스를 완료하기 위한 정책 설정 클래스 지정
                .<String, String>chunk (5)

                // 데이터 로드
                .reader(new ListItemReader<>(Arrays.asList("item1", "item2", "item3", "item4", "item5")))

                // 데이터 가공
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        Thread.sleep(300);
                        System.out.println("item : " + item);
                        return "my " + item;
                    }
                })

                // 데이터 저장
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        Thread.sleep(300);
                        System.out.println("items : " + items);
                    }
                })

                // Item이 JMS, Message Queue Server 와 같은 트랜젝션 외부에서 읽혀지고 캐시할 것인지 여부, 기본값은 false
                //.readerIsTransactionalQueue()

                // Chunk 리스너 등록
                // .listener()

                .build();
    }



    @Bean
    public Step chunkStep2_(){
        return stepBuilderFactory.get("chunkStep2_")
                .<Customer, Customer>chunk(3)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
     public ItemReader itemReader(){
        List<Customer> list = new ArrayList<>();
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        Customer customer3 = new Customer();
        customer1.setName("sun");
        customer2.setName("exg");
        customer3.setName("asd");

        list.add(customer1);
        list.add(customer2);
        list.add(customer3);
        return new CustomItemReader(list);
    }

    @Bean
     public ItemProcessor<Customer, Customer> itemProcessor(){
        return new CustomItemProcessor();
    }

    @Bean
     public ItemWriter<? extends Customer> itemWriter(){
        return new CustomItemWriter();
    }

}
