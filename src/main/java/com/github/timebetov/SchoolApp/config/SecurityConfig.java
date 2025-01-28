package com.github.timebetov.SchoolApp.config;

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

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/saveMsg"))
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers("/dashboard").authenticated()
                                .requestMatchers("/displayMessages").hasRole("ADMIN")
                                .requestMatchers("/closeMsg").hasRole("ADMIN")
                                .requestMatchers("/", "/home").permitAll()
                                .requestMatchers("/about").permitAll()
                                .requestMatchers("/contact").permitAll()
                                .requestMatchers("/courses").permitAll()
                                .requestMatchers("/saveMsg").permitAll()
                                .requestMatchers("/holidays/**").permitAll()
                                .requestMatchers("/assets/**").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/logout").permitAll())
                .formLogin(loginConf -> loginConf.loginPage("/login")
                                .defaultSuccessUrl("/dashboard")
                                .failureUrl("/login?error=true").permitAll())
                .logout(logoutConf -> logoutConf.logoutSuccessUrl("/login?logout=true")
                                .invalidateHttpSession(true)
                                .permitAll())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManagerService() {

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("12345"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("54321"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
