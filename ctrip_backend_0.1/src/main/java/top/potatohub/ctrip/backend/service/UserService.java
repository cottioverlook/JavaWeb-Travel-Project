package top.potatohub.ctrip.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.User;

@Service
public interface UserService {
    public static final int SUCCESS = 200;
    public static final int USER_NOT_FOUND_ERROR = 404;
    public static final int AUTH_FAILED_ERROR = 401;
    public static final int EMAIL_USED_ERROR = 410;
    public static final int PHONE_USED_ERROR = 411;
    public static final int UNKNOWN_TYPE_ERROR = 412;

    List<User> getList();

    User getUser(String authType, String identification);

    int update(User user);

    int delete(String id);
}
