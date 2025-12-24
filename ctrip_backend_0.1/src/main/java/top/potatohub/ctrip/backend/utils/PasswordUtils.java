package top.potatohub.ctrip.backend.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    private static final String PRIVATE_KEY = "when can a potato become a tomato?";
    private static final String ALGORITHM = "SHA-512";

    public static String getHash(String password){
        if (password == null) {
            return null;
        }
        try {
            // Hash & convert to string.
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(PRIVATE_KEY.getBytes(StandardCharsets.UTF_8)); // Salt
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(ALGORITHM + " not available", e);
        }
    }

    public static String hashPassword(String password) {
        return getHash(password);
    }
}
