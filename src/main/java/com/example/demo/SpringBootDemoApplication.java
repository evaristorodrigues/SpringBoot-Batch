package com.example.demo;


import java.io.File;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.demo.model.Person;
import com.example.demo.steps.Step1Configuration;
import com.example.demo.steps.Step2Configuration;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBootDemoApplication {
	
	
	@Bean
	Job job(JobBuilderFactory jbf, StepBuilderFactory sbf, Step1Configuration step1Configuration, Step2Configuration step2Configuration) {
		Step s1 = sbf.get("file-db")
				.<Person,Person> chunk(100)
				.reader(step1Configuration.fileReader(null))
				.writer(step1Configuration.jdbcWriter(null))
				.build();
		
		Step s2 = sbf.get("db-file")
				.<Map<Integer,Integer>,Map<Integer,Integer>> chunk(1000)
				.reader(step2Configuration.jdbcReader(null))
		        .writer(step2Configuration.fileWriter(null))
		        .build();
				
		return jbf.get("etl")
				.incrementer(new RunIdIncrementer())
				.start(s1)
				.next(s2)
				.build();
	}

	public static void main(String[] args) {
		System.setProperty("input", "file://"+new File("in.cvs").getAbsolutePath());
		System.setProperty("output", "file://"+new File("out.cvs").getAbsolutePath());
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

}
