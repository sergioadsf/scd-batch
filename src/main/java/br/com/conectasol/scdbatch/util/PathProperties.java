package br.com.conectasol.scdbatch.util;

import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "scd")
public class PathProperties {

	private String url;
	private String port;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		Logger.getRootLogger().info("Porta: "+port);
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		Logger.getRootLogger().info("Url: "+url);
		this.url = url;
	}

}
