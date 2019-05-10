package ru.sbrf.study.service;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.sbrf.study.service.layers.RestService;

import javax.sql.DataSource;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@Configuration
public class Application {

	private static final String ENV_DB_URL = "db.url";
	private static final String ENV_DB_USERNAME = "db.username";
	private static final String ENV_DB_PASSWORD = "db.password";

	@Bean
	public DataSource dataSource() {
		final Map<String, String> env = System.getenv();
		return DataSourceBuilder
				.create()
				.type(MysqlDataSource.class)
				.url(env.get(ENV_DB_URL))
				.username(env.get(ENV_DB_USERNAME))
				.password(env.get(ENV_DB_PASSWORD))
				.build();
	}

	@Configuration
	public static class JerseyConfig extends ResourceConfig {

		public JerseyConfig() {
			register(RestService.class);
			register(new LoggingFeature(Logger.getLogger("http-endpoints"), Level.INFO, LoggingFeature.Verbosity.PAYLOAD_TEXT, null));
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
