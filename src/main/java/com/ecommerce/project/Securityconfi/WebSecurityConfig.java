package com.ecommerce.project.Securityconfi;


import com.ecommerce.project.Repositries.RolesRepository;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Rolse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.ecommerce.project.Repositries.UserRepository;
import com.ecommerce.project.Security.jwt.AuthEntryPoint;
import com.ecommerce.project.Security.jwt.AuthTokenFIlter;
import com.ecommerce.project.SecurityServices.UserDetailsServiceImpl;
import com.ecommerce.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint authEntryPoint;

    @Autowired
    private AuthTokenFIlter authTokenFIlter;


    // ==============================
    // DAO AUTHENTICATION PROVIDER
    // ==============================

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    // ==============================
    // AUTHENTICATION MANAGER
    // ==============================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {

        return authConfig.getAuthenticationManager();
    }


    // ==============================
    // PASSWORD ENCODER
    // ==============================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    // ==============================
    // SECURITY FILTER CHAIN
    // ==============================

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // Disable CSRF
                .csrf(csrf -> csrf.disable())


                // Exception handling
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                authEntryPoint
                        )
                )


                // Stateless JWT authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // Authorization
                .authorizeHttpRequests(auth -> auth

                        // Authentication endpoints
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // H2 Console
                        .requestMatchers(
                                "/h2-console/**"
                        ).permitAll()

                        // Spring error endpoint
                        .requestMatchers(
                                "/error"
                        ).permitAll()

                        // Swagger
                        .requestMatchers(
                                "/v3/api-docs/**"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui/**"
                        ).permitAll()

                        // Test endpoints
                        .requestMatchers(
                                "/api/test/**"
                        ).permitAll()

                        // Images
                        .requestMatchers(
                                "/images/**"
                        ).permitAll()

                        // Sign in
                        .requestMatchers(
                                "/signin"
                        ).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )


                // Authentication provider
                .authenticationProvider(
                        authenticationProvider()
                )


                // JWT filter
                .addFilterBefore(
                        authTokenFIlter,
                        UsernamePasswordAuthenticationFilter.class
                )


                // H2 Console uses frames
                .headers(headers ->
                        headers.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig
                                        ::sameOrigin
                        )
                );


        return http.build();
    }


    // ==============================
    // WEB SECURITY CUSTOMIZER
    // ==============================

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return web -> web.ignoring().requestMatchers(

                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"

        );
    }

    @Bean
    public CommandLineRunner initData(
            UserRepository userRepository,
            RolesRepository rolesRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1. Seed Roles if they don't exist
            if (rolesRepository.count() == 0) {
                Rolse userRole = new Rolse();
                userRole.setRoleName(AppRole.ROLE_USER);
                rolesRepository.save(userRole);

                Rolse adminRole = new Rolse();
                adminRole.setRoleName(AppRole.ROLE_ADMIN);
                rolesRepository.save(adminRole);

                Rolse sellerRole = new Rolse();
                sellerRole.setRoleName(AppRole.ROLE_SELLER);
                rolesRepository.save(sellerRole);

                System.out.println("=================================");
                System.out.println("DEFAULT ROLES CREATED SUCCESSFULLY");
                System.out.println("=================================");
            }

            // 2. Seed Default User if it doesn't exist
            if (!userRepository.existsByUsername("user1")) {
                User user = new User(
                        "user1",
                        passwordEncoder.encode("password1"),
                        "user1@gmail.com"
                );
                userRepository.save(user);

                System.out.println("=================================");
                System.out.println("USER CREATED SUCCESSFULLY");
                System.out.println("Username: user1");
                System.out.println("Password: password1");
                System.out.println("Email: user1@gmail.com");
                System.out.println("=================================");
            }
        };
    }





}

