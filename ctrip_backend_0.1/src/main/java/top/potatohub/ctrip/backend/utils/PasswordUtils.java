package top.potatohub.ctrip.backend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtils {
    private static final String LEGACY_PRIVATE_KEY = "when can a potato become a tomato?";
    private static final String LEGACY_ALGORITHM = "SHA-512";
    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private PasswordUtils() {
    }

    public static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password is required");
        }
        return PASSWORD_ENCODER.encode(password);
    }

    public static boolean matches(String password, String storedHash) {
        if (password == null || storedHash == null) {
            return false;
        }
        if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
            return PASSWORD_ENCODER.matches(password, storedHash);
        }
        byte[] expected = legacyHash(password).getBytes(StandardCharsets.UTF_8);
        byte[] actual = storedHash.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(expected, actual);
    }

    public static boolean needsRehash(String storedHash) {
        return storedHash == null || !storedHash.startsWith("$2");
    }

    private static String legacyHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance(LEGACY_ALGORITHM);
            md.update(LEGACY_PRIVATE_KEY.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(LEGACY_ALGORITHM + " not available", e);
        }
    }
}
