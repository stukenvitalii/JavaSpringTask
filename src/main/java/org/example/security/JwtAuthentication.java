package org.example.security;

import lombok.Getter;
import org.example.model.Admin;
import org.example.model.Company;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Getter
public class JwtAuthentication implements Authentication {
	private final Admin admin;
	private final Company company;
	private boolean authenticated = true;

	public JwtAuthentication(Admin admin, Company company) {
		this.admin = admin;
		this.company = company;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return admin.getLogin();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
		this.authenticated = isAuthenticated;
	}

	@Override
	public String getName() {
		return admin.getLogin();
	}
}