package top.potatohub.ctrip.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final byte[] secretKey;
    private final long expirationTime;

    public JwtUtils(
            @Value("${app.jwt.secret:}") String secret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationTime) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET must contain at least 32 bytes");
        }
        this.secretKey = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationTime = expirationTime;
    }

    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        JwtBuilder builder = Jwts.builder();
        builder.setClaims(claims);
        builder.setExpiration(new Date(System.currentTimeMillis() + expirationTime));
        builder.signWith(SIGNATURE_ALGORITHM, secretKey);
        return builder.compact();
    }

    public Claims parseToken(String token) {
        JwtParser parser = Jwts.parser();
        parser.setSigningKey(secretKey);
        return parser.parseClaimsJws(token).getBody();
    }
}
