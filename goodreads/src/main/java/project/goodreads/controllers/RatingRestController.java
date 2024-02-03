package project.goodreads.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import project.goodreads.dto.RatingDto;
import project.goodreads.dto.RatingWithIdDto;
import project.goodreads.models.Rating;
import project.goodreads.repositories.RatingRepository;
import project.goodreads.services.RatingService;
import project.goodreads.services.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@Transactional
public class RatingRestController {

    private final RatingRepository ratingRepository;
    private final RatingService ratingService;
    private final SearchService<Rating> searchService;

    public RatingRestController(RatingRepository ratingRepository, RatingService ratingService) {
        this.ratingRepository = ratingRepository;
        this.ratingService = ratingService;
        this.searchService = new SearchService<>(ratingRepository);
    }

    @GetMapping
    public List<RatingWithIdDto> getAll(@RequestParam(required = false) String search) {

        List<Rating> ratings = searchService.getItems(search, Rating.class);
        List<RatingWithIdDto> ratingDtos = ratings.stream().map(r -> Rating.toRatingWithIdDto(r)).toList();

        return ratingDtos;
    }

    @GetMapping("/{id}")
    public RatingWithIdDto getOne(@PathVariable Long id) {

        var rating = ratingService.getRating(id);

        return Rating.toRatingWithIdDto(rating);
    }

    @PostMapping
    public ResponseEntity<RatingWithIdDto> createRating(@Valid @RequestBody RatingDto ratingDto) {

        var rating = ratingService.addRating(ratingDto.getStars(), ratingDto.getBookId(), ratingDto.getUserId());

        return ResponseEntity.status(201).body(Rating.toRatingWithIdDto(rating));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RatingWithIdDto> updateRating(@PathVariable Long id,
            @Valid @RequestBody RatingDto ratingDto) {

        var rating = ratingService.getRating(id);
        ratingService.updateRating(rating, ratingDto.getStars(), ratingDto.getBookId(), ratingDto.getUserId());

        return ResponseEntity.ok(Rating.toRatingWithIdDto(rating));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {

        if (id != null)
            ratingRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
