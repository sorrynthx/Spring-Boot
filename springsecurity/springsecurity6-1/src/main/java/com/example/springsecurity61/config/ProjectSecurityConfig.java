package com.example.springsecurity61.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectSecurityConfig {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
									  .requestMatchers("/notices", "/contact").permitAll())
				.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}
	
	/*
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		
		// Approach1 passwordEncoder 없이 테스트 (deprecated 뜸)
		UserDetails admin = User.withDefaultPasswordEncoder()
								.username("admin")
								.password("123123")
								.authorities("admin")
								.build();
		
		UserDetails user = User.withDefaultPasswordEncoder()
							   .username("user")
							   .password("123123")
							   .authorities("read")
							   .build();
		
		
		// Approach 2 NoOpPasswordEncoder Bean 사용
		UserDetails admin = User.withUsername("admin")
				                .password("123132")
				                .authorities("admin")
				                .build();
        UserDetails user = User.withUsername("user")
				                .password("123123")
				                .authorities("read")
				                .build();
		
		return new InMemoryUserDetailsManager(admin, user);
	}
	*/	

	 /**
     * NoOpPasswordEncoder is not recommended for production usage.
     * Use only for non-prod.
     *
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
	
	/*
	 * @Bean public UserDetailsService userDetailsService(DataSource dataSource) {
	 * return new JdbcUserDetailsManager(dataSource); }
	 */
    
}
