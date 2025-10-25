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
@EnableMethodSecurity // Mantenemos esto por si quieres volver a usar @PreAuthorize luego
public class SecurityConfig {

    private final ApplicationContext applicationContext;

    public SecurityConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtRequestFilter jwtRequestFilter = applicationContext.getBean(JwtRequestFilter.class);

        http.cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // --- ¡SEGURIDAD DESHABILITADA PARA DEMO! ---
            .authorizeHttpRequests(auth -> auth
                // Permitir TODAS las peticiones a CUALQUIER ruta sin autenticación ni roles
                .requestMatchers("/**").permitAll()

                /* --- REGLAS ANTERIORES COMENTADAS ---
                // --- 1. RUTAS PÚBLICAS ---
                .requestMatchers("/api/auth/login").permitAll()

                // --- 2. RUTAS DE CLIENTE ---
                .requestMatchers(HttpMethod.GET, "/api/clientes/profile/me").hasAnyAuthority("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/pagos/status/me").hasAnyAuthority("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/pagos/historial/me").hasAnyAuthority("CLIENTE")
                .requestMatchers(HttpMethod.GET, "/api/productos").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO") // Clientes pueden ver productos
                .requestMatchers(HttpMethod.POST, "/api/ventas").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO") // Clientes pueden comprar
                .requestMatchers(HttpMethod.PUT, "/api/personas/**").hasAnyAuthority("CLIENTE", "ADMINISTRADOR", "EMPLEADO") // Clientes editan su perfil

                // --- 3. RUTAS DE ADMIN/EMPLEADO ---
                .requestMatchers("/api/dashboard/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                .requestMatchers("/api/usuarios/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") // Empleados podrían ver lista? Ajustar si es necesario
                .requestMatchers(HttpMethod.GET, "/api/personas/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") // Para buscar al crear
                .requestMatchers(HttpMethod.POST, "/api/personas/unified").hasAnyAuthority("ADMINISTRADOR") // Solo Admin crea unificado
                .requestMatchers("/api/clientes/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                .requestMatchers("/api/empleados/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") // Empleados gestionan empleados? Solo admin quizás?
                .requestMatchers("/api/membresias/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                .requestMatchers("/api/planes/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                .requestMatchers("/api/pagos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO")
                .requestMatchers("/api/productos/**").hasAnyAuthority("ADMINISTRADOR", "EMPLEADO") // CRUD de productos

                // Cualquier otra ruta requiere autenticación por defecto
                .anyRequest().authenticated()
                 --- FIN REGLAS ANTERIORES --- */
            )
            // Aunque permitamos todo, el filtro JWT sigue intentando leer el token si existe
            // para establecer el contexto de seguridad (aunque no se use para autorización aquí)
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

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
        // Esto quita el prefijo "ROLE_"
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

