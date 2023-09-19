package com.example.springsecurity65.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.springsecurity65.filter.CsrfCookieFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class ProjectSecurityConfig {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
		// 핸들러는 CSRF 토큰을 요청 속성(attribute)으로 설정하는 역할
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		
		// CSRF 토큰의 이름을 "_csrf"로 설정. 즉, 요청이 들어올 때 "_csrf"라는 이름의 요청 속성(attribute)에 CSRF 토큰 값이 저장
        requestHandler.setCsrfRequestAttributeName("_csrf");
		
		// requireExplicitSave(false)는 보안 컨텍스트가 명시적으로 저장되어야 하는지 여부를 설정
		http.securityContext((context) -> context
                
				// false로 설정하면, 명시적으로 저장하지 않아도 됨.
				.requireExplicitSave(false))
		
				// sessionCreationPolicy(SessionCreationPolicy.ALWAYS)는 항상 새로운 세션을 생성하도록 설정합니다. 
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
		
			// http://localhost:4200 에 대한 CORS 허용
        	.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                config.setAllowedMethods(Collections.singletonList("*"));
                config.setAllowCredentials(true);
                config.setAllowedHeaders(Collections.singletonList("*"));
                config.setMaxAge(3600L);
                return config;
            }
        }))
		// csrf 비활성화 (사이트 요청 위조) ---> csrf 토큰이 없어도 서버는 응답
		// 스프링에서는 csrf 기본은 활성화 (보안 목적) ---> csrf 토큰을 url에 포함해야 서버는 응답 
		//.csrf((csrf) -> csrf.disable()) ---> CSRF 비활성화 
		
		// CSRF 활성화 (조건)
		.csrf((csrf) -> csrf.csrfTokenRequestHandler(requestHandler)							// CSRF 토큰을 요청 핸들러에 설정
							.ignoringRequestMatchers("/contact", "/register")					// 특정 URL 경로(/contact, /register)에 대해서는 CSRF 검증을 무시하도록 설정
							.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))// CSRF 토큰을 어떻게 저장할지 설정하는 부분 (쿠키를 사용하며, HttpOnly 속성을 false로 설정 ->  JavaScript에서 쿠키에 접근) 
							
							// CsrfCookieFilter 필터( 이 필터는 요청마다 한 번씩 실행되는 OncePerRequestFilter를 상속받아 구현) 추가 (Basic 인증이 처리된 후에 이 필터가 실행)
							.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
		
                .authorizeHttpRequests((requests)->requests
                        .requestMatchers("/myAccount","/myBalance","/myLoans","/myCards", "/user").authenticated()
                        .requestMatchers("/notices","/contact","/register").permitAll())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    
}
