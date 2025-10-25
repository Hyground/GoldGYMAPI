package com.goldgym.api.config;

import com.goldgym.api.jwt.JwtRequestFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; 
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final ApplicationContext applicationContext;

    public SecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    // --- ESTE ES EL MÉTODO CORREGIDO Y SEGURO ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtRequestFilter jwtRequestFilter = applicationContext.getBean(JwtRequestFilter.class);

        http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        
                        // --- 1. RUTAS PÚBLICAS ---
                        .requestMatchers("/api/auth/login").permitAll()

                        // --- 2. RUTAS ESPECÍFICAS DE CLIENTE (NO SE TOCAN) ---
                        .requestMatchers(HttpMethod.GET, "/api/clientes/profile/me").hasAnyAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pagos/status/me").hasAnyAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pagos/historial/me").hasAnyAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/productos").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/api/ventas").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers(HttpMethod.PUT, "/api/personas/**").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO")

                        
                        // --- 3. RUTAS DEL PANEL DE ADMIN (AHORA TAMBIÉN PARA EMPLEADO) ---
                        .requestMatchers("/api/dashboard/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/usuarios/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers(HttpMethod.GET, "/api/personas/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") 
                        .requestMatchers(HttpMethod.POST, "/api/personas/unified").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") 

                        
                        // --- 4. RUTAS GENERALES DE GESTIÓN (YA COMPARTIDAS) ---
                        .requestMatchers("/api/clientes/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/empleados/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/membresias/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/pagos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") 
                        .requestMatchers("/api/planes/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") 

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // --- FIN DEL MÉTODO ---


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); 
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // NOTA: Idealmente, cambia "*" por la URL de tu frontend en producción
        configuration.setAllowedOrigins(Arrays.asList("*")); 
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

