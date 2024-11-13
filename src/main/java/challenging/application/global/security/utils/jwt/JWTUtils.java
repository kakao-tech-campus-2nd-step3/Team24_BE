package challenging.application.global.security.utils.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static challenging.application.domain.auth.constant.AuthConstant.*;

@Component
public class JWTUtils {
    private final SecretKey secretKey;
    private final Long accessExpiredTime = 60 * 60 * 24 * 1000L;
    private final Long refreshExpiredTime = 60 * 60 * 48 * 1000L;

    public JWTUtils(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String generateAccessToken(String uuid, String role) {

        return Jwts.builder()
                .claim(CATEGORY, ACCESS_TOKEN)
                .claim(UUID, uuid)
                .claim(ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiredTime))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String uuid, String role) {

        return Jwts.builder()
                .claim(CATEGORY, REFRESH_TOKEN)
                .claim(UUID, uuid)
                .claim(ROLE, role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshExpiredTime))
                .signWith(secretKey)
                .compact();
    }

    public Map<String, String> getJWTInformation(String token) {
        Map<String, String> information = new HashMap<>();

        information.put(CATEGORY, getCategory(token));
        information.put(UUID, getUUID(token));
        information.put(ROLE, getRole(token));

        return information;
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("category", String.class);
    }

    public String getUUID(String uuid) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(uuid).getPayload()
                .get("uuid", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get("role", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
    }

    public Long getRefreshExpiredTime() {
        return refreshExpiredTime;
    }
}
