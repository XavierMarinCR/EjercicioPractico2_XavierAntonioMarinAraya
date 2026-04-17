
package com.fidelitas.plataforma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/login", "/acceso_denegado",
        "/css/**", "/js/**", "/webjars/**"
    };
    public static final String[] ADMIN_URLS = {
        "/usuario/nuevo", "/usuario/guardar",
        "/usuario/modificar/**", "/usuario/eliminar/**",
        "/usuario/listado", "/usuario/buscar/**",
        "/rol/nuevo", "/rol/guardar",
        "/rol/modificar/**", "/rol/eliminar/**",
        "/rol/listado"
    };
    public static final String[] ADMIN_OR_ORGANIZADOR_URLS = {
        "/evento/nuevo", "/evento/guardar",
        "/evento/modificar/**", "/evento/eliminar/**"
    };
    public static final String[] ADMIN_OR_ORGANIZADOR_OR_CLIENTE_URLS = {
        "/evento/listado", "/evento/buscar/**"
    };
    
     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(ADMIN_OR_ORGANIZADOR_URLS).hasAnyRole("ADMIN", "ORGANIZADOR")
                .requestMatchers(ADMIN_OR_ORGANIZADOR_OR_CLIENTE_URLS).hasAnyRole("ADMIN", "ORGANIZADOR", "CLIENTE")
                .anyRequest().authenticated()
        ).formLogin(form -> form // Configuración de formulario de login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(logout -> logout // Configuración de logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(exceptions -> exceptions // Manejo de excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(session -> session // Configuración de sesiones
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    
    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder build,
            @Lazy PasswordEncoder passwordEncoder,
            @Lazy UserDetailsService userDetailsService) throws Exception {
        build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
