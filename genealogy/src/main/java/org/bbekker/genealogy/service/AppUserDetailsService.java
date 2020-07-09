package org.bbekker.genealogy.service;

import java.util.ArrayList;

import org.bbekker.genealogy.common.AppConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// Use one hard-coded user for now.
		return new User(AppConstants.ADMIN_ID, new StandardPasswordEncoder().encode(AppConstants.ADMIN_ID), new ArrayList<GrantedAuthority>());
	}

}
