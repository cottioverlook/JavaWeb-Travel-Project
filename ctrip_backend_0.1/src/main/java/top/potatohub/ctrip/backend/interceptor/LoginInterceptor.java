package top.potatohub.ctrip.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.service.TokenBlacklistService;
import top.potatohub.ctrip.backend.utils.JwtUtils;

import java.io.IOException;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String requestURI = request.getRequestURI();
        System.out.println("Incoming request: " + request.getMethod() + " " + requestURI);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        Claims claims;
        
        // Check if token is present
        if (token == null || token.isEmpty()) {
             System.out.println("Token missing for path: " + requestURI);
             sendErrorResponse(response, "Token missing");
             return false;
        }

        // Verify token
        if (token != null && !token.isEmpty()) {
            try {
                // Remove prefix if exist.
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }

                if (tokenBlacklistService.isBlacklisted(token)) {
                    sendErrorResponse(response, "Token invalidated");
                    return false;
                }

                System.out.println(token);
                claims = JwtUtils.parseToken(token);
                // Success
                String id = claims.get("userId", String.class);
                request.setAttribute("userId", id);
                return true;
            } catch (ExpiredJwtException e) {
                // Expired
                sendErrorResponse(response, "Token expired");
                return false;
            } catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                // Invalid
                sendErrorResponse(response, "Invalid token");
                return false;
            } catch (Exception e) {
                // Other errors
                sendErrorResponse(response, "Token validation failed");
                return false;
            }
        }else{
            // Token missing
            System.out.println("Token missing for path: " + request.getRequestURI());
            sendErrorResponse(response, "Token missing");
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        Result<String> result = Result.error(message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
