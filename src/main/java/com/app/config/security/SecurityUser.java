package com.app.config.security;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.stream.Collectors;


public class SecurityUser extends User {
	private static final long serialVersionUID = 1L;
	private com.app.domain.user.User user;

	public SecurityUser(com.app.domain.user.User user) {
		super(user.getEmail(), user.getPassword(), Arrays.asList(new SimpleGrantedAuthority(user.getRole().getKey())));
		this.user = user;
	}
	public com.app.domain.user.User getMember() {
		return user;
	}

}
