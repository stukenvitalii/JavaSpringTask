package org.example.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.example")
public class AppConfig {

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:postgresql://localhost:5432/mydatabase");
		config.setUsername("user");
		config.setPassword("password");
		config.setDriverClassName("org.postgresql.Driver");
		config.setMaximumPoolSize(10);

		return new HikariDataSource(config);
	}

	@Bean
	public JdbcClient jdbcClient(DataSource dataSource) {
		return JdbcClient.create(dataSource);
	}
}
