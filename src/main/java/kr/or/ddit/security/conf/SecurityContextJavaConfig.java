package kr.or.ddit.security.conf;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityContextJavaConfig {
	 @Autowired
	 private AuthenticationManagerBuilder authenticationManagerBuilder;
	 
	 @PostConstruct
	 public void init() {
		 authenticationManagerBuilder.eraseCredentials(false);
	 }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailService(PasswordEncoder encoder) {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("a001").password(encoder.encode("java")).roles("USER").build());
		manager.createUser(User.withUsername("c001").password(encoder.encode("java")).roles("ADMIN").build());
		return manager;
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
						.antMatchers("/resources/**");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.anonymous().authorities("ROLE_ANONYMOUS").and()
				.authorizeHttpRequests(authorize -> 
						authorize
								.antMatchers("/public/**").permitAll()
//								.antMatchers(HttpMethod.GET, "/board/**").permitAll()
//								.antMatchers("/board/**").authenticated()
								.anyRequest().permitAll()
				).formLogin()
				.and()
				.csrf().disable();
		return http.build();
	}
	
	@Bean
	public HandlerMappingIntrospector handlerIntrospector() {
		return new HandlerMappingIntrospector();
	}
}









