package io.springbatch.springbatch.operator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@RestController
public class JobOperatorController {
    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobExplorer jobExplorer;


    @GetMapping("/batch/start")
    private String start() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {

        // ============== 여러 개의 Job 을 동시에 실행시키기 ===============
        // JobRegistry 에 등록된 Job 이름을 가져온다 
        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();){

            Job job =  jobRegistry.getJob(iterator.next()); // 기존에 한 번은  실행이 되어야 값을 가져올 수 있다 (DB에 데이터가 있어야함)
            System.out.println(job.getName());
//          jobOperator.start(job.getName(), "id=" + Math.random()); // job 이름, job parameter

        }

        // JopOperator 를 통해 JOb을 실행한다
        jobOperator.start("helloJob", "id=" + Math.random()); // job 이름, job parameter_parameter 값은 계속 바꿔줘야한다
        return "BATCH START";
    }



    @GetMapping("/batch/stop")
    private String stop() throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        Set<JobExecution> runningJobExecutions = new HashSet<>();

        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();) {
            // 현재 실행중인 job 가져오기
            runningJobExecutions = jobExplorer.findRunningJobExecutions(iterator.next());
        }


        if(runningJobExecutions.size() == 0) {
            System.out.println("실행중인 Job이 없습니다");
        } else {
            JobExecution jobExecution = runningJobExecutions.iterator().next();
            System.out.println("현재 실행중인 Job Id : " + jobExecution.getId());

            // job 중단
            jobOperator.stop(jobExecution.getId()); // jobExecutionId 를 입력
        }
        return "BATCH STOP";
    }



    // Job 이 실패한 경우만 가능
    @GetMapping("/batch/restart")
    private String restart() throws NoSuchJobExecutionException, JobInstanceAlreadyCompleteException, NoSuchJobException, JobParametersInvalidException, JobRestartException {

        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();){
            JobInstance lastJobInstance = jobExplorer.getLastJobInstance(iterator.next());
            System.out.println("실행에 실패한 Job Id : " + lastJobInstance.getId());

            jobOperator.restart(lastJobInstance.getId());
        }

        return "BATCH RESTART";
    }

}
