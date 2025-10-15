package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Company;
import org.example.model.Customer;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

	private final JdbcClient jdbcClient;

	public Optional<Customer> findByPhone(String phone, Company company) {
		String sql = "SELECT id, fio, phone, companyid FROM customers WHERE phone = ? AND companyid = ?";

		return jdbcClient.sql(sql)
				.param(phone)
				.param(company.getId())
				.query(Customer.class).optional();
	}
}
