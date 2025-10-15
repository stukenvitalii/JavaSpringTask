package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Admin;
import org.example.model.Company;
import org.example.util.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");
		String token = jwtUtil.extractTokenFromHeader(authHeader);

		if (token != null && jwtUtil.validateToken(token)) {
			// Извлекаем целые объекты из токена
			Admin admin = jwtUtil.getAdmin(token);
			Company company = jwtUtil.getCompany(token);

			JwtAuthentication authentication = new JwtAuthentication(admin, company);

			SecurityContextHolder.getContext().setAuthentication(authentication);
			log.debug("Set authentication for user: {}, company: {}", admin.getLogin(), company.getCompanyId());
		}

		filterChain.doFilter(request, response);
	}
}
