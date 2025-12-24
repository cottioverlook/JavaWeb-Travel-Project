package top.potatohub.ctrip.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class JwtUtils {
    private static final byte[] SECRET_KEY = "What's the difference between potato & tomato?".getBytes(StandardCharsets.UTF_8);
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final long EXPIRATION_TIME = 86400000;

    // Generate Token
    public static String generateToken(String userID) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userID);

        // Create JWT builder.
        JwtBuilder builder = Jwts.builder();
        builder.setClaims(claims);
        Date expTime = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        builder.setExpiration(expTime);
        builder.signWith(SIGNATURE_ALGORITHM, SECRET_KEY);
        return builder.compact();
    }

    // Parse Token
    public static Claims parseToken(String token) {
        JwtParser parser = Jwts.parser();
        parser.setSigningKey(SECRET_KEY);
        return parser.parseClaimsJws(token).getBody();
    }
}
