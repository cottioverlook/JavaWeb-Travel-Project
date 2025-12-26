package top.potatohub.ctrip.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attraction {
    private String id;
    private String title;
    private BigDecimal price;
    private String tag;
    private Double rating;
    private String comments;
    private String imgUrl;
    private String cityId;
}
