package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    String id;
    String email;
    String phone;
    String name;
    String passwordHash;
    String avatarUrl;
    String authLevel;
    Date createdAt;
    Date updatedAt;

}
