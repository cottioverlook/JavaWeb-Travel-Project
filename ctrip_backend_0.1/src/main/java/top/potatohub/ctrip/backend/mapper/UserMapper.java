package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> getUsers();

    User getByEmailPwd(String email,
                       String passwordHash);

    User getByPhonePwd(String phone,
                       String passwordHash);

    User getById(String id);

    List<User> getByName(String username);

    User getByEmail(String mail);

    User getByPhone(String phone);

    int add(User user);

    int update(User user);

    int delete(String id);
}
