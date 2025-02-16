package com.github.timebetov.SchoolApp.config;

import com.github.timebetov.SchoolApp.model.Person;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf
                        .ignoringRequestMatchers("/saveMsg")
                        .ignoringRequestMatchers("/public/**")
                        .ignoringRequestMatchers("/api/**")
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/displayMessages/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/closeMsg").hasRole("ADMIN")
                        .requestMatchers("/student/**").hasRole("STUDENT")
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers("/dashboard").authenticated()
                        .requestMatchers("/displayProfile").authenticated()
                        .requestMatchers("/updateProfile").authenticated()
                        .requestMatchers("/", "/home").permitAll()
                        .requestMatchers("/about").permitAll()
                        .requestMatchers("/contact").permitAll()
                        .requestMatchers("/courses").permitAll()
                        .requestMatchers("/saveMsg").permitAll()
                        .requestMatchers("/holidays/**").permitAll()
                        .requestMatchers("/assets/**").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/logout").permitAll()
                        .requestMatchers("/public/**").permitAll()
                )
                .formLogin(loginConf -> loginConf
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard")
                        .failureUrl("/login?error=true").permitAll()
                )
                .logout(logoutConf -> logoutConf
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
