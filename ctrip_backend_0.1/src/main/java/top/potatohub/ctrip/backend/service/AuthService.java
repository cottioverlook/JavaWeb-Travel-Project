package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.User;

public interface AuthService {
    public static final int SUCCESS = 200;
    public static final int USER_NOT_FOUND_ERROR = 404;
    public static final int AUTH_FAILED_ERROR = 401;
    public static final int EMAIL_USED_ERROR = 410;
    public static final int PHONE_USED_ERROR = 411;
    public static final int UNKNOWN_TYPE_ERROR = 412;
    public static final String DEFAULT_USER_NAME = "Potato";

    int auth(String authType, String identification, String password);

    String generateToken(String authType, String identification);

    int register(String type, String identification, String password);

    int changePassword(String id, String oldPassword, String newPassword);

    int resetPassword(String type, String identification, String password);

}
