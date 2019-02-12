package br.com.conectasol.scdbatch.resource;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.conctasol.annotation.MIndex;
import br.com.conctasol.annotation.util.IndiceUtil;
import br.com.conectasol.scdbatch.model.Folha;

@RestController
@RequestMapping(path = "folha")
public class FolhaResource {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@GetMapping(path = "/createindex")
	public String createindex() {

		try {
			MIndex mIndex = Folha.class.getAnnotation(MIndex.class);
			String nome = "";
			if (mIndex != null) {
				nome = mIndex.name();
			}
			HttpClient httpclient = HttpClients.createDefault();
			HttpPost httppost = new HttpPost("http://localhost:8083/indice/" + nome);
			httppost.setHeader("Accept", "application/json");

			String json = new IndiceUtil().convertJson(Folha.class);

			StringEntity entity = new StringEntity(json, "UTF8");
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(entity);
			HttpResponse response = httpclient.execute(httppost);

			return json;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

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
			return "ok - " + elapsed;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

}
