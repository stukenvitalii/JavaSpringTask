package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.CustomerResponse;
import org.example.model.Company;
import org.example.model.Customer;
import org.example.repository.CompanyRepository;
import org.example.repository.CustomerRepository;
import org.example.util.PhoneFormatter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

	private final CompanyRepository companyRepository;
	private final CustomerRepository customerRepository;
	private final PhoneFormatter phoneFormatter;

	/**
	 * Ищет клиента по номеру телефона с учетом маски компании
	 * @param phone введенный номер телефона (может быть как "+7 (913) 608-55-55", так и "79136085555")
	 * @param companyId ID компании из JWT токена
	 * @return CustomerResponse с отформатированным номером или null если не найден
	 * @throws IllegalArgumentException если номер не соответствует формату маски компании
	 * @throws RuntimeException если компания не найдена
	 */
	public CustomerResponse findByPhone(String phone, int companyId) {
		// Получаем компанию с её маской
		Company company = companyRepository.getCompanyById(companyId);
		if (company == null) {
			log.error("Company not found with id: {}", companyId);
			throw new RuntimeException("Компания не найдена");
		}

		// Приводим номер телефона к формату маски (выбросит IllegalArgumentException если формат неверный)
		String formattedPhone;
		try {
			formattedPhone = phoneFormatter.formatPhone(phone, company.getPhoneMask());
			log.debug("Phone formatted: {} -> {}", phone, formattedPhone);
		} catch (IllegalArgumentException e) {
			log.warn("Invalid phone format: {} for mask: {}", phone, company.getPhoneMask());
			throw e;
		}

		// Нормализуем отформатированный номер для поиска в БД (только цифры)
		String normalizedPhone = phoneFormatter.normalizePhone(formattedPhone);
		log.debug("Normalized phone for search: {}", normalizedPhone);

		// Ищем клиента в БД по нормализованному номеру
		Customer customer = customerRepository.findByPhone(normalizedPhone, company)
				.orElse(null);

		if (customer == null) {
			log.info("Customer not found with phone: {} (normalized: {}) for company: {}", phone, normalizedPhone, companyId);
			return null;
		}

		log.info("Customer found: {} with phone: {}", customer.getFio(), formattedPhone);

		// Возвращаем DTO с отформатированным номером
		return new CustomerResponse(customer.getFio(), formattedPhone);
	}
}
