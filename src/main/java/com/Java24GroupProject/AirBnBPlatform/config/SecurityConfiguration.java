package com.Java24GroupProject.AirBnBPlatform.config;

import com.Java24GroupProject.AirBnBPlatform.filters.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

//This class is a part of the site security, configures method and endpoint access based on roles among other things
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    //create Authentication manager which dictates the authentication process
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //main config for security filter and rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS config (is not applied in postman, first in action in frontend)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF, disable in dev (NB! should only be disabled during development, not when going live)
                .csrf(csrf -> csrf.disable())
                // define access to different url destinations
                .authorizeHttpRequests(auth -> auth
                        //only admin can access things under url admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //only users with host role can access listing creation
                        .requestMatchers("/listings/**").hasRole("HOST")
                        //only logged-in users (any role) can access
                        .requestMatchers("/user/**", "/booking/**").hasAnyRole("USER", "HOST", "ADMIN")
                        //any user can access, incl. login page and search page (search for listings)
                        .requestMatchers("/auth/**", "/search/**").permitAll()
                        //all other urls, only logged-in users
                        .anyRequest().authenticated()
                )
                // due session due to jwt statuslessness
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //adds jwt filter before the standard filer
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //cors config - again, only frontend (NB! may need to change this for front end if it causes issues)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        //only allow requests from our client
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:8080"));

        //set allowed request mappings, (could include only GET, if it is an unalterable webpage)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        //set allowed headers (we will allow all)
        configuration.setAllowedHeaders(List.of("*"));

        configuration.setAllowCredentials(true);

        //set that we can get the header, so that we can extract it
        configuration.setExposedHeaders(List.of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    //hash and salt algo (common strength is 10-12)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
