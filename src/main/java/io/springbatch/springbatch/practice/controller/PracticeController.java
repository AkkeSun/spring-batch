package io.springbatch.springbatch.practice.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@RestController
public class PracticeController {

    @Autowired
    private JobOperator jobOperator;

    @GetMapping("/file/start")
    public String job1Start() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {

        jobOperator.start("fileJob__", "requestDate=" + "20220215");
        return "BATCH START";
    }

    @GetMapping("/api/start")
    public String job2Start() throws NoSuchJobException, JobInstanceAlreadyExistsException, JobParametersInvalidException {


        jobOperator.start("apiJob", "id=" + Math.random());
        return "BATCH START";
    }
}
