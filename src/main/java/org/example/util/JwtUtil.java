package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
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

	public JwtUtil() {
		this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Генерирует JWT токен с информацией о компании и админе
	 */
	public String generateToken(int adminId, String login, int companyId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("adminId", adminId);
		claims.put("login", login);
		claims.put("companyId", companyId);

		return Jwts.builder()
				.claims(claims)
				.subject(login)
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
	 * Извлекает ID админа из токена
	 */
	public Integer getAdminId(String token) {
		return extractClaims(token).get("adminId", Integer.class);
	}

	/**
	 * Извлекает логин из токена
	 */
	public String getLogin(String token) {
		return extractClaims(token).get("login", String.class);
	}

	/**
	 * Извлекает ID компании из токена
	 */
	public Integer getCompanyId(String token) {
		return extractClaims(token).get("companyId", Integer.class);
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
}

