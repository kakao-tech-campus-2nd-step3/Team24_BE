package challenging.application.auth.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static challenging.application.auth.utils.AuthConstant.*;

@Component
public class JWTUtils {
    private final SecretKey secretKey;
    private final Long accessExpiredTime = 600000L;
    private final Long refreshExpiredTime = 86400000L;

    public JWTUtils(@Value("${spring.jwt.secret}") String secret){
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String generateAccessToken(String email, String role) {

        return Jwts.builder()
                .claim(CATEGORY, ACCESS_TOKEN)
                .claim(EMAIL, email)
                .claim(ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiredTime))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String email, String role) {

        return Jwts.builder()
                .claim(CATEGORY, REFRESH_TOKEN)
                .claim(EMAIL, email)
                .claim(ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiredTime))
                .signWith(secretKey)
                .compact();
    }

    public Map<String, String> getJWTInformation(String token){
        Map<String, String> information = new HashMap<>();

        information.put(CATEGORY, getCategory(token));
        information.put(EMAIL, getEmail(token));
        information.put(ROLE, getRole(token));

        return information;
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }

    public String getEmail(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }
    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }
    public Long getRefreshExpiredTime() {
        return refreshExpiredTime;
    }
}
