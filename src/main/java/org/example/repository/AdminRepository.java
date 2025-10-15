package org.example.repository;

import lombok.RequiredArgsConstructor;
import org.example.model.Admin;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AdminRepository {
	private final JdbcClient jdbcClient;

	public Optional<Admin> findByLoginAndPassword(String login, String password) {
		String sql = "SELECT id, login, password FROM admins WHERE login = ? AND password = ?";

		return jdbcClient.sql(sql)
				.param(login)
				.param(password)
				.query((rs, rowNum) -> new Admin(
						rs.getInt("id"),
						rs.getString("login"),
						rs.getString("password")
				))
				.optional();
	}

	public Optional<Integer> getCompanyIdByAdminId(int adminId) {
		String sql = "SELECT companyid FROM admins WHERE id = ?";

		return jdbcClient.sql(sql)
				.param(adminId)
				.query(Integer.class)
				.optional();
	}
}

