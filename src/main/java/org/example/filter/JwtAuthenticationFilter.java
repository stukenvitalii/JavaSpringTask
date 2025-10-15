package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.util.JwtUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements Filter {
	private final JwtUtil jwtUtil;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestURI = httpRequest.getRequestURI();

		// Пропускаем аутентификацию для /auth/login
		if (requestURI.contains("/auth/login") || requestURI.equals("/")) {
			chain.doFilter(request, response);
			return;
		}

		String authHeader = httpRequest.getHeader("Authorization");
		String token = jwtUtil.extractTokenFromHeader(authHeader);

		if (token == null || !jwtUtil.validateToken(token)) {
			log.error("Unauthorized access attempt to {}", requestURI);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType("application/json");
			httpResponse.getWriter().write("{\"error\": \"Unauthorized - Invalid or missing token\"}");
			return;
		}

		// Добавляем информацию из токена в атрибуты запроса
		httpRequest.setAttribute("adminId", jwtUtil.getAdminId(token));
		httpRequest.setAttribute("companyId", jwtUtil.getCompanyId(token));
		httpRequest.setAttribute("login", jwtUtil.getLogin(token));

		chain.doFilter(request, response);
	}
}
