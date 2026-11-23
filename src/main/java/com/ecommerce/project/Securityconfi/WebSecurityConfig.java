package com.ecommerce.project.Securityconfi;

import com.ecommerce.project.Repositries.RolesRepository;
import com.ecommerce.project.Repositries.UserRepository;
import com.ecommerce.project.Security.jwt.AuthEntryPoint;
import com.ecommerce.project.Security.jwt.AuthTokenFIlter;
import com.ecommerce.project.SecurityServices.UserDetailsServiceImpl;
import com.ecommerce.project.model.AppRole;
import com.ecommerce.project.model.Rolse;
import com.ecommerce.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

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

    @Value("${frontend.url:}")
    private String frontendUrl;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:5173");
        origins.add("http://localhost:3000");
        if (frontendUrl != null && !frontendUrl.isBlank() && !origins.contains(frontendUrl)) {
            origins.add(frontendUrl);
        }

        config.setAllowedOrigins(origins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                authEntryPoint
                        )
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/signin").permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(
                        authenticationProvider()
                )
                .addFilterBefore(
                        authTokenFIlter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .headers(headers ->
                        headers.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig
                                        ::sameOrigin
                        )
                );

        return http.build();
    }

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