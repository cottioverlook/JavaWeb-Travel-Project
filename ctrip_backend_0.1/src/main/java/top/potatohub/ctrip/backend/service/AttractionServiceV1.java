package top.potatohub.ctrip.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.potatohub.ctrip.backend.entities.Attraction;
import top.potatohub.ctrip.backend.mapper.AttractionMapper;
import java.util.List;

@Service
public class AttractionServiceV1 implements AttractionService {

    @Autowired
    private AttractionMapper attractionMapper;

    @Override
    public List<Attraction> getRecommendations(int limit) {
        return attractionMapper.getRecommendations(limit);
    }

    @Override
    public List<Attraction> searchAttractions(String keyword, int page, int size) {
        int offset = (page - 1) * size;
        return attractionMapper.searchAttractions(keyword, offset, size);
    }

    @Override
    public Attraction getAttractionById(String id) {
        return attractionMapper.findById(id);
    }
}
