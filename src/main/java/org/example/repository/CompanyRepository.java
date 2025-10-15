package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Company;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CompanyRepository {
	private final JdbcClient jdbcClient;

	public Company getCompanyById(int companyId) {
		String sql = "SELECT id, phonemask FROM companies WHERE id = ?";

		return jdbcClient.sql(sql)
				.param(companyId)
				.query(rs -> {
					if (rs.next()) {
						return new Company(
								rs.getInt("id"),
								rs.getString("phoneMask")
						);
					}
					return null;
				});
	}
}
