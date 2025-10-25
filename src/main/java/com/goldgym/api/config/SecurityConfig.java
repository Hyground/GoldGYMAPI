package com.goldgym.api.config;

import com.goldgym.api.jwt.JwtRequestFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// --- IMPORTACIÓN AÑADIDA ---
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

    // --- ESTE ES EL MÉTODO CORREGIDO ---
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtRequestFilter jwtRequestFilter = applicationContext.getBean(JwtRequestFilter.class);

        http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        
                        // --- 1. RUTAS PÚBLICAS ---
                        // Permitir login sin autenticación
                        .requestMatchers("/api/auth/login").permitAll()

                        // --- 2. RUTAS DE CLIENTE ---
                        // (Reglas específicas deben ir ANTES que las generales)
                        // Permisos para ver su dashboard
                        .requestMatchers(HttpMethod.GET, "/api/clientes/profile/me").hasAnyAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pagos/status/me").hasAnyAuthority("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pagos/historial/me").hasAnyAuthority("CLIENTE")
                        
                        // Permisos para la tienda
                        .requestMatchers(HttpMethod.GET, "/api/productos").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers(HttpMethod.POST, "/api/ventas").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO")

                        // Permiso para que el cliente edite SU PROPIO perfil (el controlador debe validar que sea él mismo)
                        .requestMatchers(HttpMethod.PUT, "/api/personas/**").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO")


                        // --- 3. RUTAS DE ADMIN/EMPLEADO ---
                        // (Estas son tus reglas generales que ya tenías)
                        .requestMatchers("/api/clientes/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/empleados/**").hasAnyAuthority("ADMINISTRADOR")
                        .requestMatchers("/api/membresias/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/pagos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                        .requestMatchers("/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") // Para POST, PUT, DELETE de productos
                        .requestMatchers("/api/planes/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") // Asumiendo que tienes este endpoint

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    // --- FIN DEL MÉTODO CORREGIDO ---


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
        // Esto quita el prefijo "ROLE_" (tus reglas usan "CLIENTE" en lugar de "ROLE_CLIENTE")
        return new GrantedAuthorityDefaults(""); 
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Permitir todas las peticiones (para desarrollo)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}