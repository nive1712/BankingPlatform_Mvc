package com.example.mvc.config;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.mvc.service.CustomUserDetailsService;

 
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.example.mvc")
public class SecurityConfig {
	private final CustomUserDetailsService customUserService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserService) {
        this.customUserService = customUserService;
    }
 
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .loginProcessingUrl("/process_login")
                .defaultSuccessUrl("/dashboard", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .permitAll()
                .failureUrl("/login?error=true")
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/WEB-INF/views/**").permitAll()
                .requestMatchers("/register", "/registration").permitAll()
                .requestMatchers("/dashboard").permitAll()
              
                .requestMatchers("/applyLoan").hasAnyRole("USER")
                .requestMatchers("/home").hasAnyRole("admin", "USER")
                .requestMatchers("/atm/**").hasAnyRole("USER")
               
                .requestMatchers("/netbanking/**").hasRole("USER")
                
                .requestMatchers("/admin/**").hasRole("admin")
                .requestMatchers("/user/**").hasRole("USER")
                
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .build();
    }
    

 
    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService).passwordEncoder(passwordEncoder());
    }
}
