package br.com.conectasol.scdbatch.folha.writer;

import java.util.List;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import br.com.conctasol.annotation.MIndex;
import br.com.conectasol.scdbatch.model.Folha;
import br.com.conectasol.scdbatch.util.PathProperties;

@Configuration
public class FolhaWriter implements ItemWriter<String> {
	
	@Autowired
	private PathProperties pathProperties;

	@Override
	public void write(List<? extends String> items) throws Exception {
		MIndex mIndex = Folha.class.getAnnotation(MIndex.class);
		String nome = "";
		if (mIndex != null) {
			nome = mIndex.name();
		}

		StringBuilder sb = new StringBuilder();

		try (CloseableHttpClient httpclient = HttpClients.createDefault();) {
			HttpPost httppost = new HttpPost(String.format("http://%s:%s/documento/%s", pathProperties.getUrl(), pathProperties.getPort(), nome) );;
			httppost.setHeader("Accept", "application/json");

			for (String item : items) {
				sb.append(item);
			}

			StringEntity entity = new StringEntity(sb.toString(), "UTF8");
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			httppost.setEntity(entity);
			httpclient.execute(httppost);
		}
	}

}
