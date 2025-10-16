package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.model.Admin;
import org.example.model.Company;
import org.example.repository.AdminRepository;
import org.example.repository.CompanyRepository;
import org.example.util.JwtUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AdminRepository adminRepository;
	private final CompanyRepository companyRepository;
	private final JwtUtil jwtUtil;

	public LoginResponse login(LoginRequest loginRequest) {
		// Ищем админа по логину и паролю
		Admin admin = adminRepository.findByLoginAndPassword(
				loginRequest.getLogin(),
				loginRequest.getPassword()
		).orElseThrow(() -> new RuntimeException("Invalid credentials"));

		// Получаем ID компании для админа
		Integer companyId = adminRepository.getCompanyIdByAdminId(admin.getId())
				.orElseThrow(() -> new RuntimeException("Company not found for admin"));

		// Получаем полный объект Company
		Company company = companyRepository.getCompanyById(companyId);
		if (company == null)
			throw new RuntimeException("Company not found");

		// Генерируем JWT токен с целыми объектами Admin и Company
		String token = jwtUtil.generateToken(admin, company);

		return new LoginResponse(token);
	}
}
