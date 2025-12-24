package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainStation {
    private String id;
    private String name;
    private String cityId;
}
