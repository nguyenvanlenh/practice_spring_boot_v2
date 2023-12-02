package com.watermelon.security;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.watermelon.model.entity.User;
import com.watermelon.repository.UserRepository;
@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException("User not found");
		  // Manually initialize roles
	    Hibernate.initialize(user.getListRoles());
		return new CustomUserDetails().mapUserToUserDetail(user);
	}
	

}
