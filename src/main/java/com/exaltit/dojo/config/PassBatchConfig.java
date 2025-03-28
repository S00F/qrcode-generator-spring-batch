package com.exaltit.dojo.config;


import com.exaltit.dojo.batch.PassItemProcessor;
import com.exaltit.dojo.batch.PassItemReader;
import com.exaltit.dojo.batch.PassItemWriter;
import com.exaltit.dojo.model.Pass;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class PassBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PassItemProcessor passItemProcessor;
    private final PassItemWriter passItemWriter;
    private final PassItemReader passItemReader;




    @Bean
    public Job importPassJob(Step step1) {
        return new JobBuilder("importPassJob", jobRepository)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository)
                .<Pass, Pass>chunk(10, transactionManager)
                .reader(passItemReader)
                .processor(passItemProcessor)
                .writer(passItemWriter)
                .build();
    }
}
