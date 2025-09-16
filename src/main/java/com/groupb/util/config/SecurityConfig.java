package com.groupb.util.config;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import com.groupb.util.jwt.JwtAuthenticationFilter;

/**
 *
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired(required = false)
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	//配置密码编码器
	@Bean
	public PasswordEncoder passwordEncoder(){
		//使用BCrypt强哈希函数加密
		return new BCryptPasswordEncoder(12);
	}

	//基础安全过滤器链配置：放行 /login 等公开端点，关闭CSRF并改用无状态会话
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login").permitAll()
						.requestMatchers("/static/**", "/resources/**").permitAll()
						.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.anyRequest().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		if (jwtAuthenticationFilter != null) {
			http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		}
		return http.build();
	}
}
