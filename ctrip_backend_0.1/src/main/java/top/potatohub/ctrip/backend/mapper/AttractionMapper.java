package top.potatohub.ctrip.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.potatohub.ctrip.backend.entities.Attraction;
import java.util.List;

@Mapper
public interface AttractionMapper {
    List<Attraction> getRecommendations(int limit);
    List<Attraction> searchAttractions(@Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);
    Attraction findById(String id);
    void createTableIfNotExists();
    void insertAttraction(Attraction attraction);
    void deleteAll();
    int count();
}
