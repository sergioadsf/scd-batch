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
	
	private static final String[] NAMES_V1 = new String[] { "ANOMES", "NOMECARGOSECUNDARIO", "SIMBOLOCARGOSECUNDARIO",
			"NIVELSALARIALSECUNDARIO", "SIMBOLOCARGO", "NOMECARGO", "NIVELSALARIAL", "ORGAO", "NOMESERVIDOR",
			"VALORCORTETETO", "VALORDECIMOTERCEIRO", "VALORDEMAISDESCONTOS", "VALORFERIAS", "VALORLIQUIDO",
			"VALORPROVENTO", "CODORGAO", "VALORPROVENTOMES" };

	private static final String[] NAMES_V2 = new String[] { "CODORGAO", "ANOMES", "NOMECARGOSECUNDARIO",
			"SIMBOLOCARGOSECUNDARIO", "NIVELSALARIALSECUNDARIO", "SIMBOLOCARGO", "NOMECARGO", "NIVELSALARIAL", "ORGAO",
			"NOMESERVIDOR", "VALORCORTETETO", "VALORDECIMOTERCEIRO", "VALORDEMAISDESCONTOS", "VALORFERIAS",
			"VALORLIQUIDO", "VALORPROVENTO", "VALORPROVENTOMES" };

	private static final String DELIMITER = ";";

	@Value("classpath*:input/v1/FolhaPagamento_*.csv")
	private Resource[] inputResourcesV1;

	@Value("classpath*:input/v2/FolhaPagamento_*.csv")
	private Resource[] inputResourcesV2;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public MultiResourceItemReader<Folha> multiResourceItemReaderV1() {
		MultiResourceItemReader<Folha> resourceItemReader = new MultiResourceItemReader<Folha>();
		resourceItemReader.setResources(inputResourcesV1);
		resourceItemReader.setDelegate(readerV1());
		return resourceItemReader;
	}

	@Bean
	public MultiResourceItemReader<Folha> multiResourceItemReaderV2() {
		MultiResourceItemReader<Folha> resourceItemReader = new MultiResourceItemReader<Folha>();
		resourceItemReader.setResources(inputResourcesV2);
		resourceItemReader.setDelegate(readerV2());
		return resourceItemReader;
	}

	@Bean
	public FlatFileItemReader<Folha> readerV1() {
		return new FlatFileItemReaderBuilder<Folha>()
				.name("folhapagamento").linesToSkip(1).delimited()
				.delimiter(DELIMITER)
				.names(NAMES_V1)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<Folha>() {
					{
						setTargetType(Folha.class);
					}
				}).build();
	}

	@Bean
	public FlatFileItemReader<Folha> readerV2() {
		return new FlatFileItemReaderBuilder<Folha>()
				.name("folhapagamento")
				.linesToSkip(1).delimited()
				.delimiter(DELIMITER)
				.names(NAMES_V2)
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
	public Step stepV1(FolhaWriter writer) {
		return stepBuilderFactory
				.get("step1")
				.<Folha, String>chunk(1000)
				.reader(multiResourceItemReaderV1())
				.processor(processor())
				.writer(writer)
				.build();
	}

	@Bean
	public Step stepV2(FolhaWriter writer) {
		return stepBuilderFactory
				.get("step2")
				.<Folha, String>chunk(1000)
				.reader(multiResourceItemReaderV2())
				.processor(processor())
				.writer(writer)
				.build();
	}

	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step stepV1, Step stepV2) {
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(stepV1)
				.next(stepV2)
				.end()
				.build();
	}
}
