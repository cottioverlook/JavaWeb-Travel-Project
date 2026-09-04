package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.User;
import top.potatohub.ctrip.backend.mapper.UserMapper;
import top.potatohub.ctrip.backend.utils.JwtUtils;
import top.potatohub.ctrip.backend.utils.PasswordUtils;

import java.util.UUID;

@Service
public class AuthServiceV1 implements AuthService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public int auth(String authType, String identification, String password) {
        User user;
        try {
            user = userService.getUser(authType, identification);
        }catch (UnknownError e){
            return UNKNOWN_TYPE_ERROR;
        }

        if (user == null) {
            return USER_NOT_FOUND_ERROR;
        }

        if (!PasswordUtils.matches(password, user.getPasswordHash())) {
            return AUTH_FAILED_ERROR;
        }
        if (PasswordUtils.needsRehash(user.getPasswordHash())) {
            User passwordUpgrade = new User();
            passwordUpgrade.setId(user.getId());
            passwordUpgrade.setPasswordHash(PasswordUtils.hashPassword(password));
            userMapper.update(passwordUpgrade);
        }
        return SUCCESS;
    }

    @Override
    public String generateToken(String authType, String identification) {
        User user;
        try{
            user = userService.getUser(authType, identification);
        }catch (UnknownError e){
            return null;
        }

        if(user == null) {
            return null;
        }
        return jwtUtils.generateToken(user.getId());
    }


    @Override
    public int register(String type, String identification, String password) {
        System.out.println(type);
        switch (type) {
            case "email":
                if (userMapper.getByEmail(identification) != null) { return EMAIL_USED_ERROR; }
                break;
            case "phone":
                if (userMapper.getByPhone(identification) != null) { return PHONE_USED_ERROR; }
                break;
            default:
                return UNKNOWN_TYPE_ERROR;
        }
        String passwordHash = PasswordUtils.hashPassword(password);
        User user = new User();
        user.setId(UUID.randomUUID().toString()); // Generate UUID
        user.setPasswordHash(passwordHash);
        user.setName(DEFAULT_USER_NAME);
        if ("email".equals(type)) {
            user.setEmail(identification);
        } else {
            user.setPhone(identification);
        }
        if (userMapper.add(user) != 0) {
            return SUCCESS;
        }
        return USER_NOT_FOUND_ERROR;
    }

    @Override
    public int changePassword(String id, String oldPassword, String newPassword) {
        // Verify old password first
        User existingUser = userService.getUser("id", id);
        if (existingUser == null) {
            return USER_NOT_FOUND_ERROR;
        }
        
        if (!PasswordUtils.matches(oldPassword, existingUser.getPasswordHash())) {
            return AUTH_FAILED_ERROR;
        }
        
        // Update password
        String newHash = PasswordUtils.hashPassword(newPassword);
        User user = new User();
        user.setId(id);
        user.setPasswordHash(newHash);
        
        int code = userMapper.update(user);
        if(code != 1){
            return 500;
        }
        return SUCCESS;
    }

}
