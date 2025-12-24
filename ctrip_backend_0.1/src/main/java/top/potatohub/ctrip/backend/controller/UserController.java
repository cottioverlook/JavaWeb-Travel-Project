package top.potatohub.ctrip.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.User;
import top.potatohub.ctrip.backend.entities.UserDTO;
import top.potatohub.ctrip.backend.mapper.UserMapper;
import top.potatohub.ctrip.backend.service.UserService;
import top.potatohub.ctrip.backend.utils.JwtUtils;
import top.potatohub.ctrip.backend.utils.PasswordUtils;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/users/self/profile")
    public Result<String> updateProfile(@RequestBody User user, HttpServletRequest request) {
        String id = (String)request.getAttribute("userId");
        user.setId(id);
        int code = userService.update(user);
        switch (code) {
            case UserService.USER_NOT_FOUND_ERROR:
                return Result.error(code, "User not found!");
            case UserService.SUCCESS:
                return Result.success();
            default:
                return Result.error(500, "Server inner error!");
        }
    }

    @GetMapping("/users/self/profile")
    public Result<UserDTO> getSelfProfile(HttpServletRequest request) {
        String id = (String)request.getAttribute("userId");
        User user = userService.getUser("id", id);
        if(user == null) {
            return Result.error(404, "User not found!");
        }
        return Result.success(new UserDTO(user));
    }

    @GetMapping("/users/{id}/profile")
    public Result<UserDTO> getProfile(@PathVariable String id) {
        User user = userService.getUser("id", id);
        if(user == null) {
            return Result.error(404, "User not found!");
        }
        return Result.success(new UserDTO(user));
    }

}
