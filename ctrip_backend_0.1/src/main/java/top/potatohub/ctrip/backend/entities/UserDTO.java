package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    String id;
    String email;
    String phone;
    String name;
    String avatarUrl;
    String authLevel;

    public UserDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.authLevel = user.getAuthLevel();
    }

}
