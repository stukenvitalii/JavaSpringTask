package org.example.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();

		// Разрешенные источники (в продакшене указать конкретные домены)
		config.setAllowedOriginPatterns(List.of("*"));

		// Разрешенные HTTP методы
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

		// Разрешенные заголовки
		config.setAllowedHeaders(Arrays.asList("*"));

		// Разрешить отправку credentials (cookies, authorization headers)
		config.setAllowCredentials(true);

		// Заголовки, которые клиент может читать
		config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

		// Максимальное время кеширования preflight запросов
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}

