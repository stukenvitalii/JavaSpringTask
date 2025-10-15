package org.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.model.Admin;
import org.example.model.Company;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
	// Секретный ключ для подписи JWT (в продакшене должен быть в конфигурации)
	private static final String SECRET_KEY = "your-secret-key-must-be-at-least-256-bits-long-for-hs256-algorithm";
	private static final long EXPIRATION_TIME = 3600000; // 1 час в миллисекундах

	private final SecretKey key;
	private final ObjectMapper objectMapper;

	public JwtUtil() {
		this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
		this.objectMapper = new ObjectMapper();
	}

	/**
	 * Генерирует JWT токен с информацией о компании и админе
	 */
	public String generateToken(Admin admin, Company company) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("admin", convertToMap(admin));
		claims.put("company", convertToMap(company));

		return Jwts.builder()
				.claims(claims)
				.subject(admin.getLogin())
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key)
				.compact();
	}

	/**
	 * Извлекает все claims из токена
	 */
	public Claims extractClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	/**
	 * Извлекает объект Admin из токена
	 */
	public Admin getAdmin(String token) {
		Claims claims = extractClaims(token);
		Map<String, Object> adminMap = claims.get("admin", Map.class);
		return objectMapper.convertValue(adminMap, Admin.class);
	}

	/**
	 * Извлекает объект Company из токена
	 */
	public Company getCompany(String token) {
		Claims claims = extractClaims(token);
		Map<String, Object> companyMap = claims.get("company", Map.class);
		return objectMapper.convertValue(companyMap, Company.class);
	}

	/**
	 * Проверяет валидность токена
	 */
	public boolean validateToken(String token) {
		try {
			Claims claims = extractClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Извлекает токен из заголовка Authorization
	 */
	public String extractTokenFromHeader(String authHeader) {
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}
		return null;
	}

	/**
	 * Конвертирует объект в Map для JWT claims
	 */
	private Map<String, Object> convertToMap(Object obj) {
		return objectMapper.convertValue(obj, new TypeReference<Map<String, Object>>() {});
	}
}
