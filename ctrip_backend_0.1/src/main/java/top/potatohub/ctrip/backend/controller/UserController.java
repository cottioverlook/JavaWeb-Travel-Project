package top.potatohub.ctrip.backend.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.User;
import top.potatohub.ctrip.backend.entities.UserDTO;
import top.potatohub.ctrip.backend.entities.UpdateProfileRequest;
import top.potatohub.ctrip.backend.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/users/self/profile")
    public Result<String> updateProfile(@RequestBody UpdateProfileRequest profile, HttpServletRequest request) {
        String id = (String)request.getAttribute("userId");
        User user = new User();
        user.setId(id);
        user.setName(profile.getName());
        user.setAvatarUrl(profile.getAvatarUrl());
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
