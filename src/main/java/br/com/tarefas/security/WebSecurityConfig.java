package br.com.tarefas.security;

import java.time.LocalDateTime;


import java.util.Arrays;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    private final AuthTokenFilter authTokenFilter;

    public WebSecurityConfig(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        	.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/tarefa/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/tarefa/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/tarefa/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/tarefa/**").hasRole("ADMIN")
                
                .requestMatchers(HttpMethod.GET, "/categoria/**")
                .hasAnyRole("USER", "ADMIN")

                .requestMatchers(HttpMethod.POST, "/categoria/**")
                .hasRole("ADMIN")

                .requestMatchers(HttpMethod.DELETE, "/categoria/**")
                .hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .exceptionHandling(ex ->
                ex.authenticationEntryPoint(authenticationEntryPoint())
            )
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
            
        return http.build();
    }
    
    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081"));
    	configuration.setAllowedMethods(Arrays.asList("*"));
    	configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
    	
    } 	
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                "{\"timestamp\":\"" + LocalDateTime.now() + "\"," +
                "\"status\":401," +
                "\"error\":\"Unauthorized\"," +
                "\"message\":\"Acesso negado - autenticação necessária\"}"
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}