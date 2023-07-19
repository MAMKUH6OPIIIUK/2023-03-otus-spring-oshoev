package ru.otus.spring.homework.oke.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/book/**", "/comment/**").authenticated()
                        .anyRequest().permitAll()
                )
                .rememberMe().key("MyLittlePony").tokenValiditySeconds(60 * 60 * 24)
                .and()
                .formLogin()
                .usernameParameter("lib_username")
                .passwordParameter("lib_password");
        return http.build();
    }

    @SuppressWarnings("deprecation")
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MessageDigestPasswordEncoder("MD5");
    }
}
