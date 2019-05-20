package ru.sbrf.study.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.sbrf.study.service.layers.RestService;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@Configuration
public class Application {

	private static final String ENV_DB_URL = "db.url";
	private static final String ENV_DB_USERNAME = "db.username";
	private static final String ENV_DB_PASSWORD = "db.password";

	@Value("${db.url}")
	private String dbUrl;
	@Value("${db.username}")
	private String dbUsername;
	@Value("${db.password}")
	private String dbPassword;

	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	public DataSource dataSource() {
		final Map<String, String> env = System.getenv();
		return DataSourceBuilder
				.create()
				.type(MysqlDataSource.class)
				/* данные для подключения к СУБД, не используя Hibernate
				.url(env.get(ENV_DB_URL))
				.username(env.get(ENV_DB_USERNAME))
				.password(env.get(ENV_DB_PASSWORD))
				*/
				//данные для подключения к СУБД, используя Hibernate - берутся из application.property
				.url(dbUrl)
				.username(dbUsername)
				.password(dbPassword)
				.build();
	}

	@Bean(destroyMethod = "close")
	public Client restClient() {
		return ClientBuilder.newBuilder().register(new JacksonJsonProvider(objectMapper)).build();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Function<String, String> serviceUrlProvider() {
		return serviceName -> System.getenv().get(serviceName + ".url");
	}

	@PostConstruct
	public void setupObjectMapper() {
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.registerModule(new Jdk8Module());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
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
