package com.example.mvc.service;

import com.example.mvc.imple.UserDaoImpl;

import com.example.mvc.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserDaoImpl userDAO;

	@Autowired
	public void setUserDAO(UserDaoImpl userDAO) {
		this.userDAO = userDAO;
	}



	

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDAO.getUserByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
				.password(user.getPassword()).roles(user.getRole()).build();
	}

}
