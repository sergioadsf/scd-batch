package br.com.conectasol.scdbatch.resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "folha")
public class FolhaResource {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@GetMapping(path = "/teste")
	public String teste() {

		try {
			long start = System.currentTimeMillis();
			jobLauncher.run(job, new JobParameters());
			long elapsed = System.currentTimeMillis() - start;
//			MIndex mIndex = Folha.class.getAnnotation(MIndex.class);
//			String nome = "";
//			if (mIndex != null) {
//				nome = mIndex.name();
//			}
//			HttpClient httpclient = HttpClients.createDefault();
//			HttpPost httppost = new HttpPost("http://localhost:8083/indice/" + nome);
//			httppost.setHeader("Accept", "application/json");
//
//			JSONObject json = new JSONObject(new IndiceUtil().convert(Folha.class));
//
//			StringEntity entity = new StringEntity(json.toString(), "UTF8");
//			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			httppost.setEntity(entity);
//			HttpResponse response = httpclient.execute(httppost);

			System.out.println(elapsed);
			return "ok - "+elapsed;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
