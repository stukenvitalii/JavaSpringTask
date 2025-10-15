package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.model.Admin;
import org.example.repository.AdminRepository;
import org.example.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AdminRepository adminRepository;
	private final JwtUtil jwtUtil;

	public LoginResponse login(LoginRequest loginRequest) {
		// Ищем админа по логину и паролю
		Admin admin = adminRepository.findByLoginAndPassword(
				loginRequest.getLogin(),
				loginRequest.getPassword()
		).orElseThrow(() -> new RuntimeException("Invalid credentials"));

		// Получаем ID компании для админа
		Integer companyId = adminRepository.getCompanyIdByAdminId(admin.getAdminId())
				.orElseThrow(() -> new RuntimeException("Company not found for admin"));

		// Генерируем JWT токен с информацией о компании и админе
		String token = jwtUtil.generateToken(
				admin.getAdminId(),
				admin.getLogin(),
				companyId
		);

		return new LoginResponse(token);
	}
}
