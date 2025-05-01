package oleksandr.jobbit_back.config;

import oleksandr.jobbit_back.utils.JwtAuthenticationFilter;
import oleksandr.jobbit_back.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Конфігураційний клас Spring Security, що визначає правила авторизації, CORS, JWT-фільтр та
 * інші налаштування безпеки для застосунку.
 *
 * @author Oleksandr Borovyk
 */
@Configuration
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    /**
     * Конструктор класу {@code SecurityConfig}, що ініціалізує залежність {@link JwtUtil}.
     *
     * @param jwtUtil Об'єкт {@link JwtUtil} для роботи з JWT. Dependency Injection.
     */
    @Autowired
    public SecurityConfig(final JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Створює та повертає бін {@link PasswordEncoder} для кодування паролів.
     * Використовує алгоритм BCrypt.
     *
     * @return Бін {@link PasswordEncoder} з BCrypt-алгоритмом.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Конфігурує ланцюг фільтрів безпеки ({@link SecurityFilterChain}) для обробки HTTP-запитів.
     * Включає налаштування CORS, відключення CSRF для певних шляхів, правила авторизації за ролями,
     * керування сесіями (без сесій) та додавання JWT-аутентифікаційного фільтра.
     *
     * @param http Об'єкт {@link HttpSecurity} для налаштування безпеки.
     * @return Збудований {@link SecurityFilterChain}.
     * @throws Exception У випадку помилки при конфігурації безпеки.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:8081"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowCredentials(true);
            config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
            return config;
        })).csrf(csrf -> csrf.ignoringRequestMatchers("/api/auth/**"))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/vacancies/all").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/candidate-dash/**").hasRole("CANDIDATE")
                        .requestMatchers("/api/recruiter-dash/**").hasRole("RECRUITER")
                        .anyRequest().authenticated())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Створює та повертає бін {@link JwtAuthenticationFilter} для обробки JWT-токенів
     * перед стандартним фільтром аутентифікації за email та паролем.
     *
     * @return Бін {@link JwtAuthenticationFilter}.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }
}
