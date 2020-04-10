/**
 * 
 */
package com.example.demo.steps;

import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.example.demo.model.Person;

/**
 * @author evaristosrodrigues
 *
 */
@Configuration
public  class Step1Configuration {
	@Bean
	public FlatFileItemReader<Person> fileReader(@Value("${input}") Resource resource){
		return new FlatFileItemReaderBuilder<Person>()
				.name("file-reader")
				.resource(resource)
				.targetType(Person.class)
				.delimited().delimiter(",").names(new String[] {"firstName","age","email"})
				.build();
	}		
	@Bean
	public JdbcBatchItemWriter<Person> jdbcWriter(DataSource dataSource){
		return new JdbcBatchItemWriterBuilder<Person>()
				.dataSource(dataSource)
				.sql("INSERT INTO PERSON( age, first_name, email) values (:age, :firstName, :email)")
				.beanMapped()
				.build();
	}
}
