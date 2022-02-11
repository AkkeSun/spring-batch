package io.springbatch.springbatch.operator;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.SimpleJob;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@RestController
public class JobOperatorController {
    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobExplorer jobExplorer;

    @GetMapping("/batch/start")
    private String start() throws NoSuchJobException {

        // JobRegistry 에 등록된 Job 이름을 가져온다 
        for(Iterator<String> iterator = jobRegistry.getJobNames().iterator(); iterator.hasNext();){
            
            // Job 이름을 가지고 Job 을 가져온다 
            SimpleJob job = (SimpleJob) jobRegistry.getJob(iterator.next());
            System.out.println(job.getName());
        }

        return "BATCH START";
    }

}
