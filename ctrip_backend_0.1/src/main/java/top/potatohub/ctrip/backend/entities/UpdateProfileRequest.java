package top.potatohub.ctrip.backend.entities;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private String avatarUrl;
}
