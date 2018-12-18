package br.com.conectasol.scdbatch.folha.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import br.com.conectasol.scdbatch.folha.processor.FolhaProcessor;
import br.com.conectasol.scdbatch.folha.writer.FolhaWriter;
import br.com.conectasol.scdbatch.listener.JobCompletionNotificationListener;
import br.com.conectasol.scdbatch.model.Folha;

@Configuration
@EnableBatchProcessing
public class FolhaConfiguration {

	@Value("classpath*:input/FolhaPagamento_*.csv")
	private Resource[] inputResources;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public MultiResourceItemReader<Folha> multiResourceItemReader() {
		MultiResourceItemReader<Folha> resourceItemReader = new MultiResourceItemReader<Folha>();
		resourceItemReader.setResources(inputResources);
		resourceItemReader.setDelegate(reader());
		return resourceItemReader;
	}
	
	@Bean
	public FlatFileItemReader<Folha> reader() {
		return new FlatFileItemReaderBuilder<Folha>()
				.name("folhapagamento")
				.linesToSkip(1)
				.delimited()
				.delimiter(";")
				.names(new String[] {
						"ANOMES", "NOMECARGOSECUNDARIO", "SIMBOLOCARGOSECUNDARIO",
						"NIVELSALARIALSECUNDARIO", "SIMBOLOCARGO", "NOMECARGO", "NIVELSALARIAL", "ORGAO",
						"NOMESERVIDOR", "VALORCORTETETO", "VALORDECIMOTERCEIRO", "VALORDEMAISDESCONTOS", "VALORFERIAS",
						"VALORLIQUIDO", "VALORPROVENTO", "CODORGAO", "VALORPROVENTOMES" 
						})
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Folha>() {
					{
						setTargetType(Folha.class);
					}
				}).build();
	}
	
	@Bean
	public FolhaProcessor processor() {
		return new FolhaProcessor();
	}
	
	@Bean
	public FolhaWriter writer() {
		return new FolhaWriter();
	}
	
	@Bean
	public Step step1(FolhaWriter writer) {
		return stepBuilderFactory
				.get("step1")
				.<Folha, String>chunk(10000)
				.reader(multiResourceItemReader())
				.processor(processor())
				.writer(writer)
				.build();
	}
	
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return jobBuilderFactory
				.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1)
				.end().build();
	}
}
