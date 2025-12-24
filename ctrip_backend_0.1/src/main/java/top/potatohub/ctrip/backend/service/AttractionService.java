package top.potatohub.ctrip.backend.service;

import top.potatohub.ctrip.backend.entities.Attraction;
import java.util.List;

public interface AttractionService {
    List<Attraction> getRecommendations(int limit);
    List<Attraction> searchAttractions(String keyword, int page, int size);
    Attraction getAttractionById(String id);
}
