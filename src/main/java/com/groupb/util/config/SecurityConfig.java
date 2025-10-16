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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.http.HttpMethod;

/**
 *
 * */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired(required = false)
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private CorsConfigurationSource corsConfigurationSource;
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
				.cors(cors -> cors.configurationSource(corsConfigurationSource)) // 添加CORS配置
				.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // 允许所有OPTIONS请求
				.requestMatchers("/api/auth/**").permitAll()   // 允许认证相关接口无需认证
								.requestMatchers("/ai/**").permitAll()
				.requestMatchers("/api/emotion/types").permitAll() // 允许获取情绪类型无需认证
				.requestMatchers("/api/emotion/recent").permitAll() // 允许获取最近情绪无需认证（开发阶段）
				.requestMatchers("/api/test/**").permitAll()   // 允许测试接口无需认证
				.requestMatchers("/api/doc").permitAll()       // 允许API文档无需认证
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
