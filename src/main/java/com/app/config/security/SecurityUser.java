package com.app.config.security;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;


public class SecurityUser extends User {
	private static final long serialVersionUID = 1L;
	private com.app.domain.User user;

	public SecurityUser(com.app.domain.User user) {
		super(user.getEmail(), user.getPassword(), user.getRoleList().stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList()));
		this.user = user;
	}
	public com.app.domain.User getMember() {
		return user;
	}

}
