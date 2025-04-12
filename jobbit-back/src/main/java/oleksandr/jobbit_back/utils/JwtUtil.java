package oleksandr.jobbit_back.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import oleksandr.jobbit_back.entity.Role;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Утилітний клас {@code JwtUtil} відповідає за генерацію, валідацію та вилучення інформації з JWT (JSON Web Tokens).
 * Використовує бібліотеку JJWT для роботи з токенами. Секретний ключ для підпису та перевірки токенів
 * зберігається як константа та використовується для ініціалізації об'єктів {@link SecretKey} та {@link JwtParser}.
 * Анотація {@code @Component} позначає цей клас як Spring-компонент.
 *
 * @author Oleksandr Borovyk
 */
@Component
public class JwtUtil {
    private static final String SECRET_KEY = "yZufvdBO5JRWPeE6H06h+FerQMRiQP369ePu6BFk1SfZE9sdtFYEu2TYy8KMJVYOCSqqUcvMGPPKcgQkcnVy2g==";
    private final SecretKey key;
    private final JwtParser jwtParser;

    /**
     * Конструктор класу {@code JwtUtil}, що ініціалізує секретний ключ та парсер JWT.
     *
     * @param environment Об'єкт {@link Environment} (хоча тут не використовується, може бути корисним для
     * конфігурації секретного ключа ззовні в майбутньому). Автоматично впроваджується Spring.
     */
    public JwtUtil(Environment environment) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    /**
     * Генерує новий JWT для вказаного email та ролі користувача.
     * Токен включає subject (email), claim "role", час видачі та термін дії (24 години).
     * Підписується з використанням секретного ключа.
     *
     * @param email Електронна пошта користувача, для якого генерується токен.
     * @param role  Роль користувача ({@link Role}), що включається в токен як claim.
     * @return Згенерований JWT у вигляді рядка.
     */
    public String generateToken(String email, Role role) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    /**
     * Витягає email користувача з наданого JWT.
     * Перевіряє підпис токена. У випадку невірного підпису викидає {@link RuntimeException}.
     *
     * @param token JWT, з якого потрібно вилучити email.
     * @return Email користувача, що міститься в subject токена.
     * @throws RuntimeException Якщо підпис JWT є недійсним.
     */
    public String extractEmail(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return claims.getSubject();
        } catch (SignatureException e) {
            throw new RuntimeException("Невірний JWT");
        }
    }

    /**
     * Витягає роль користувача з наданого JWT.
     * Перевіряє підпис токена. У випадку невірного підпису викидає {@link RuntimeException}.
     *
     * @param token JWT, з якого потрібно вилучити роль.
     * @return Роль користувача у вигляді рядка, що міститься в claims токена під ключем "role".
     * @throws RuntimeException Якщо підпис JWT є недійсним.
     */
    public String extractRole(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return claims.get("role", String.class);
        } catch (SignatureException e) {
            throw new RuntimeException("Невірний JWT");
        }
    }

}
