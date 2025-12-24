package top.potatohub.ctrip.backend.service;

import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.User;
import top.potatohub.ctrip.backend.mapper.UserMapper;
import top.potatohub.ctrip.backend.utils.JwtUtils;
import top.potatohub.ctrip.backend.utils.PasswordUtils;

import java.util.List;

@Service
public class UserServiceV1 implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getList() {
        return userMapper.getUsers();
    }

    @Override
    public User getUser(String authType, String identification) {
        switch (authType) {
            case "email":
                return userMapper.getByEmail(identification);
            case "phone":
                return userMapper.getByPhone(identification);
            case "id":
                return userMapper.getById(identification);
            default:
                throw new UnknownError();
        }
    }



    @Override
    public int update(User user) {
        if(userMapper.update(user) != 0){
            return SUCCESS;
        }
        return USER_NOT_FOUND_ERROR;
    }

    @Override
    public int delete(String id) {
        if(userMapper.delete(id) != 0){
            return SUCCESS;
        }
        return USER_NOT_FOUND_ERROR;
    }
}