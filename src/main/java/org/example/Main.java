package org.example;

import org.eclipse.jetty.server.Server;
import org.example.configuration.JettyServerConfig;

public class Main {
	public static void main(String[] args) throws Exception {
		// Создаем и запускаем Jetty сервер через конфигурацию
		Server server = JettyServerConfig.createServer(8080);

		server.start();
		System.out.println("Jetty сервер запущен на http://localhost:8080");
		System.out.println("Endpoints:");
		System.out.println("  POST http://localhost:8080/auth/login - аутентификация");
		System.out.println("  POST  http://localhost:8080/customers {search : \"phone\"} - поиск клиента");

		server.join();
	}
}