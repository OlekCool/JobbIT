package oleksandr.jobbit_back.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Фільтр {@code JwtAuthenticationFilter} є Spring Security фільтром, який перехоплює кожен вхідний HTTP-запит
 * та виконує аутентифікацію користувача на основі JWT (JSON Web Token), що передається в заголовку "Authorization".
 * Фільтр перевіряє наявність заголовка, його формат ("Bearer token"), валідність токена, витягує email та роль користувача
 * та встановлює контекст безпеки Spring Security, якщо токен є дійсним.
 * Фільтр виконується один раз для кожного запиту.
 *
 * @author Oleksandr Borovyk
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    /**
     * Конструктор класу {@code JwtAuthenticationFilter}, що приймає залежність {@link JwtUtil}.
     *
     * @param jwtUtil Утиліта для роботи з JWT (генерація, валідація, вилучення даних). Автоматично впроваджується Spring.
     */
    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Метод, який виконується для кожного вхідного HTTP-запиту.
     * Перевіряє заголовок "Authorization" на наявність JWT, валідує токен, витягує інформацію про користувача
     * та встановлює контекст безпеки Spring Security.
     *
     * @param request Об'єкт {@link HttpServletRequest}, що представляє HTTP-запит.
     * @param response Об'єкт {@link HttpServletResponse}, що представляє HTTP-відповідь.
     * @param filterChain Ланцюжок фільтрів, що дозволяє передати обробку запиту наступному фільтру в ланцюжку.
     * @throws ServletException У випадку помилки сервлета.
     * @throws IOException У випадку помилки вводу-виводу.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);

        if (email != null) {
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));  // Додаємо роль до authorities
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
