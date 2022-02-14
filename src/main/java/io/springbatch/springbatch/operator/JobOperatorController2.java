package io.springbatch.springbatch.operator;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class JobOperatorController2 {

    @Autowired
    private JobOperator jobOperator;

    /**
     * 초 분 시간 일 월 요일
     * @Scheduled(cron = "1 10 13 * * *") 매일 13시 10분 1초마다 실행
     */
    @Scheduled(cron = "*/5 * * * * *") // 5초마다 실행
    private void batchTest() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {
    //    jobOperator.start("helloJob", "id=" + Math.random());
    }
}
