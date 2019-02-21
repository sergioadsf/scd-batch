package br.com.conectasol.scdbatch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private final JdbcTemplate jdbcTemplate;
	
	private long data;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		 data = System.currentTimeMillis();
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		long data2 = System.currentTimeMillis() - data;
		
		
		
//		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
//			log.info("!!! JOB FINISHED! Time to verify the results");
//
//			jdbcTemplate.query("SELECT ano_mes, cod_orgao FROM folha",
//				(rs, row) -> new Person(
//					rs.getString(1),
//					rs.getString(2))
//			).forEach(folha -> log.info("Found <" + folha + "> in the database."));
//		}
		
		System.out.println("Tempo -> "+data2);
	}
}