package org.example.configuration;

import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.EnumSet;

/**
 * Конфигурация и запуск Jetty сервера со Spring
 */
public class JettyServerConfig {

	public static Server createServer(int port) {
		Server server = new Server(port);

		// Создаем Spring контекст
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.register(AppConfig.class);
		context.register(WebConfig.class);
		context.register(CorsConfig.class);

		// Создаем ServletContextHandler
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");

		// Добавляем ContextLoaderListener
		servletContextHandler.addEventListener(new ContextLoaderListener(context));

		// Обновляем контекст после создания ServletContext
		context.setServletContext(servletContextHandler.getServletContext());
		context.refresh();

		// Регистрируем JWT фильтр
		servletContextHandler.addFilter(
				new org.eclipse.jetty.ee10.servlet.FilterHolder(
						context.getBean(org.example.filter.JwtAuthenticationFilter.class)
				),
				"/*",
				EnumSet.of(DispatcherType.REQUEST)
		);

		// Регистрируем DispatcherServlet
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		ServletHolder servletHolder = new ServletHolder(dispatcherServlet);
		servletContextHandler.addServlet(servletHolder, "/");

		// Устанавливаем handler для сервера
		server.setHandler(servletContextHandler);

		return server;
	}
}
