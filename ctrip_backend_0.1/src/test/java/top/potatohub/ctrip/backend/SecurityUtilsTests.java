package top.potatohub.ctrip.backend;

import org.junit.jupiter.api.Test;
import top.potatohub.ctrip.backend.utils.JwtUtils;
import top.potatohub.ctrip.backend.utils.PasswordUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecurityUtilsTests {

    @Test
    void bcryptPasswordsUseUniqueSaltsAndVerify() {
        String first = PasswordUtils.hashPassword("correct horse battery staple");
        String second = PasswordUtils.hashPassword("correct horse battery staple");

        assertNotEquals(first, second);
        assertTrue(PasswordUtils.matches("correct horse battery staple", first));
        assertFalse(PasswordUtils.matches("wrong password", first));
        assertFalse(PasswordUtils.needsRehash(first));
    }

    @Test
    void jwtRoundTripUsesConfiguredSecret() {
        JwtUtils jwtUtils = new JwtUtils("test-secret-that-is-at-least-thirty-two-bytes", 60_000);
        String token = jwtUtils.generateToken("user-123");

        assertEquals("user-123", jwtUtils.parseToken(token).get("userId", String.class));
    }

    @Test
    void jwtRejectsShortSecrets() {
        assertThrows(IllegalStateException.class, () -> new JwtUtils("too-short", 60_000));
    }
}
