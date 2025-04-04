package oleksandr.jobbit_back.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    //put your secret_key for jwt authentication
    private static final String SECRET_KEY = "yZufvdBO5JRWPeE6H06h+FerQMRiQP369ePu6BFk1SfZE9sdtFYEu2TYy8KMJVYOCSqqUcvMGPPKcgQkcnVy2g==";
    private final SecretKey key;
    private final JwtParser jwtParser;

    public JwtUtil(Environment environment) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    public String extractEmail(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return claims.getSubject();
        } catch (SignatureException e) {
            throw new RuntimeException("Невірний JWT");
        }
    }

}
