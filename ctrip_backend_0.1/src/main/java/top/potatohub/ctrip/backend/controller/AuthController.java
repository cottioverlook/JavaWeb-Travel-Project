package top.potatohub.ctrip.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.service.AuthService;
import top.potatohub.ctrip.backend.service.TokenBlacklistService;
import top.potatohub.ctrip.backend.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody Map<String, String> payload) {
        System.out.println("Register request received: " + payload);
        String regType = payload.get("regType");
        String identification = payload.get("identification");
        String password = payload.get("password");

        int code = authService.register(regType, identification, password);

        switch (code) {
            case UserService.SUCCESS:
                return Result.success();
            case UserService.EMAIL_USED_ERROR:
                return Result.error(code, "Email already in use!");
            case UserService.PHONE_USED_ERROR:
                return Result.error(code, "Phone already in use!");
            case UserService.UNKNOWN_TYPE_ERROR:
                return Result.error(code, "Register type unknown!");
            default:
                return Result.error(code, "Server inner error!");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> payload) {
        String loginType = payload.get("loginType");
        String identification = payload.get("identification");
        String password = payload.get("password");

        int authCode = authService.auth(loginType, identification, password);
        if (authCode != UserService.SUCCESS) {
            switch(authCode){
                case UserService.USER_NOT_FOUND_ERROR:
                    return Result.error(UserService.USER_NOT_FOUND_ERROR, "User not found!");
                case UserService.UNKNOWN_TYPE_ERROR:
                    return Result.error(UserService.UNKNOWN_TYPE_ERROR, "Login type invalid!");
                case UserService.AUTH_FAILED_ERROR:
                    return Result.error(UserService.AUTH_FAILED_ERROR, "Incorrect username or password!");
                default:
                    return Result.error(500, "Server inner error!");
            }
        }

        String token = authService.generateToken(loginType, identification);

        if (token == null) {
            return Result.error(500, "Server inner error!");
        }
        return Result.success(token);
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenBlacklistService.addToBlacklist(token);
        }
        return Result.success("Logout successful");
    }

    @PutMapping("/changePassword")
    public Result<String> changePassword(@RequestBody Map<String, String> payload, HttpServletRequest request) {
        String oldPassword = payload.get("oldPassword");
        String newPassword = payload.get("newPassword");

        String id = (String)request.getAttribute("userId");

        int code = authService.changePassword(id, oldPassword, newPassword);
        switch (code) {
            case UserService.SUCCESS:
                return Result.success();
            case UserService.AUTH_FAILED_ERROR:
                return Result.error(code, "Incorrect oldPassword!");
            case UserService.USER_NOT_FOUND_ERROR:
                return Result.error(UserService.USER_NOT_FOUND_ERROR, "User not found!");
            default:
                return Result.error(code, "Server inner error!");
        }
    }

    @PutMapping("/resetPassword")
    public Result<String> resetPassword(@RequestBody Map<String, String> payload) {
        String authType = payload.get("authType");
        String identification = payload.get("identification");
        String newPassword = payload.get("newPassword");

        int code = authService.resetPassword(authType, identification, newPassword);
        switch (code) {
            case UserService.SUCCESS:
                return Result.success();
            case UserService.AUTH_FAILED_ERROR:
                return Result.error(code, "Authorization failed!");
            case UserService.USER_NOT_FOUND_ERROR:
                return Result.error(UserService.USER_NOT_FOUND_ERROR, "User not found!");
            default:
                return Result.error(code, "Server inner error!");
        }
    }

}
