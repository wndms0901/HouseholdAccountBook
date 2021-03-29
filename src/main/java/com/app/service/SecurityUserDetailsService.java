package com.app.service;

import java.util.Optional;

import com.app.config.security.SecurityUser;
import com.app.domain.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SecurityUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	/**
	 * 사용자 정보 조회
	 * @param username
	 * @return UserDetails
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optional = userRepository.findByEmail(username);
		if(!optional.isPresent()) {
			throw new UsernameNotFoundException(username+" 사용자 없음");
		}else {
			User user = optional.get();
			return new SecurityUser(user);
		}
	}

}
