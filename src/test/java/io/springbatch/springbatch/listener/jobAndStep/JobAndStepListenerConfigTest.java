package io.springbatch.springbatch.listener.jobAndStep;

import org.junit.runner.RunWith;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {JobAndStepListenerConfigTest.class}) // job은 하나만 등록 가능하다
public class JobAndStepListenerConfigTest {

}