 package io.springbatch.springbatch.repeatAndError.repeat;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 배치 내에 반복이 필요한 경우
 */
@Configuration
@RequiredArgsConstructor
public class RepeatConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job repeatJob(){
        return jobBuilderFactory.get("repeatJob")
//                .incrementer(new RunIdIncrementer())
                .start(repeatStep())
                .build();
    }


    @Bean
    public Step repeatStep(){
        return stepBuilderFactory.get("repeatStep")
                .<String, String>chunk (5)

                .reader(new ItemReader<String>() {
                    int num = 0;

                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        num ++;
                        return num >3 ? null : "Item "+num;
                    }
                })

                .processor(new ItemProcessor<String, String>() {

                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {

                        // 1. 세번 반복 후 종료 
                        repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(3));
                        
                        // 2. 1초간 반복 후 종료
                        // repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(1000));

                        // 3. 복수의 조건 중 하나가 완료되면 종료
                        /*
                        CompositeCompletionPolicy compositeCompletionPolicy = new CompositeCompletionPolicy();
                        CompletionPolicy [] completionPolicies = new CompletionPolicy[]{
                                new SimpleCompletionPolicy(3),
                                new TimeoutTerminationPolicy(1000)
                        };
                        compositeCompletionPolicy.setPolicies(completionPolicies);
                        repeatTemplate.setCompletionPolicy(compositeCompletionPolicy);
                        */

                        // 4. 예외 세 번 까지 반복
                        // repeatTemplate.setExceptionHandler(new SimpleLimitExceptionHandler(3));

                        repeatTemplate.iterate(new RepeatCallback() {
                            @Override
                            public RepeatStatus doInIteration(RepeatContext repeatContext)  {
                                System.out.println("=====> Repeat Template Test");
                                // throw new RuntimeException("err");
                                // return RepeatStatus.FINISHED; // 즉시 종료
                                return RepeatStatus.CONTINUABLE;
                            }
                        });

                        System.out.println( "Repeat Finish! ITEM : "  + item);
                        return "my " + item;
                    }
                })
                .writer(items -> System.out.println(items))
                .build();
    }


}
