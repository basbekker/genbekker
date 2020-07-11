package org.bbekker.genealogy.common;

import org.bbekker.genealogy.service.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@EnableWebSecurity
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Autowired
	private AppUserDetailsService appUserDetailService;

	@Value("${org.bbekker.genealogy.ldap.ldap_enabled}")
	private String LDAP_ENABLED;

	@Value("${org.bbekker.genealogy.ldap.ldap_server_url}")
	private String LDAP_SERVER_URL;

	@Value("${org.bbekker.genealogy.ldap.ldap_bind_password}")
	private String LDAP_PASSWORD;



	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin();
	}

	/*
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {

			// LDAP setup
			auth.ldapAuthentication().userDnPatterns("uid={0},cn=users").groupSearchBase("ou=groups")
			.contextSource().url(LDAP_SERVER_URL)
			.and().passwordCompare().passwordEncoder(new BCryptPasswordEncoder()).passwordAttribute(LDAP_PASSWORD);

	}
	 */

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(appUserDetailService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new StandardPasswordEncoder();
		//return NoOpPasswordEncoder.getInstance();
	}

}
