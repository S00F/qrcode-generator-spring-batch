package com.exaltit.dojo.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchJobService {
    private final JobLauncher jobLauncher;
    private final Job importPassJob;

    public void startBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            var jobExecution = jobLauncher.run(importPassJob, jobParameters);
            log.info("Batch Job started with ID: {}", jobExecution.getId());
        } catch (Exception e) {
            log.error("Error starting batch job", e);
            throw new RuntimeException("Failed to start batch job", e);
        }
    }
}
