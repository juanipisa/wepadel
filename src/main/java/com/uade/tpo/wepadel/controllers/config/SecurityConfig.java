package com.uade.tpo.wepadel.controllers.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
          .csrf(AbstractHttpConfigurer::disable)
          .exceptionHandling(ex -> ex
          .authenticationEntryPoint((request, response, authException) -> {
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
              response.setContentType("application/json");
              response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Token inválido o no proporcionado\"}");
          })
          .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
              response.setContentType("application/json");
              response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"No tenés permiso para realizar esta acción\"}");
          })
        )
          .authorizeHttpRequests(req -> req
  
              // Auth — público
              .requestMatchers("/api/v1/auth/**").permitAll()
  
              // Productos — lectura pública, escritura solo ADMIN
              .requestMatchers(HttpMethod.GET, "/productos/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/productos/**").hasAuthority("ADMINISTRADOR")
              .requestMatchers(HttpMethod.PUT, "/productos/**").hasAuthority("ADMINISTRADOR")
              
              // Imágenes — lectura pública, subida solo ADMIN
              .requestMatchers(HttpMethod.GET, "/imagenes/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/imagenes/**").hasAuthority("ADMINISTRADOR")
  
              // Descuentos — lectura pública, gestión solo ADMIN
              .requestMatchers(HttpMethod.GET, "/descuentos/**").permitAll()
              .requestMatchers(HttpMethod.POST, "/descuentos/**").hasAuthority("ADMINISTRADOR")
              .requestMatchers(HttpMethod.DELETE, "/descuentos/**").hasAuthority("ADMINISTRADOR")
  
              // Stocks — lectura pública, actualización solo ADMIN
              .requestMatchers(HttpMethod.GET, "/stocks/**").permitAll()
              .requestMatchers(HttpMethod.PUT, "/stocks/**").hasAuthority("ADMINISTRADOR")
  
              // Usuarios — solo ADMIN puede listar, ADMIN y CLIENTE pueden ver/editar el suyo
              .requestMatchers(HttpMethod.GET, "/usuarios").hasAuthority("ADMINISTRADOR")
              .requestMatchers(HttpMethod.GET, "/usuarios/**").hasAnyAuthority("ADMINISTRADOR", "CLIENTE")
              .requestMatchers(HttpMethod.PUT, "/usuarios/**").hasAnyAuthority("ADMINISTRADOR", "CLIENTE")
  
              // Carrito, Órdenes y Puntos — solo CLIENTE
              .requestMatchers("/usuarios/*/carrito/**").hasAuthority("CLIENTE")
              .requestMatchers("/usuarios/*/ordenes/**").hasAuthority("CLIENTE")
              .requestMatchers("/usuarios/*/puntos/**").hasAuthority("CLIENTE")
  
              .anyRequest().authenticated())
          .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
          .authenticationProvider(authenticationProvider)
          .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
  
      return http.build();
  }
}