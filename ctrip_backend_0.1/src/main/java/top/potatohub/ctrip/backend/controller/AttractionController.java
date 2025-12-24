package top.potatohub.ctrip.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.potatohub.ctrip.backend.common.Result;
import top.potatohub.ctrip.backend.entities.Attraction;
import top.potatohub.ctrip.backend.service.AttractionService;

import java.util.List;

@RestController
@RequestMapping("/attractions")
public class AttractionController {

    @Autowired
    private AttractionService attractionService;

    @GetMapping("/recommend")
    public Result<List<Attraction>> getRecommendations(@RequestParam(defaultValue = "4") int limit) {
        List<Attraction> list = attractionService.getRecommendations(limit);
        return Result.success(list);
    }

    @GetMapping
    public Result<List<Attraction>> searchAttractions(@RequestParam(required = false) String keyword,
                                                      @RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        List<Attraction> list = attractionService.searchAttractions(keyword, page, size);
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<Attraction> getAttraction(@PathVariable String id) {
        Attraction attraction = attractionService.getAttractionById(id);
        if (attraction != null) {
            return Result.success(attraction);
        } else {
            return Result.error("Attraction not found");
        }
    }
}
