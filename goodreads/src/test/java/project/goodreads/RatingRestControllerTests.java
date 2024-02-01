package project.goodreads;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.goodreads.controllers.rest.RatingRestController;
import project.goodreads.dto.RatingDto;
import project.goodreads.dto.RatingWithIdDto;
import project.goodreads.models.Rating;
import project.goodreads.repositories.RatingRepository;
import project.goodreads.services.ReviewService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingRestControllerTests {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private RatingRestController ratingRestController;

    @Test
    public void getAllRatings() {
        // Given
        Rating rating = new Rating();
        rating.setId(1L);
        rating.setStars(5D);
        rating.setBookId(2L);
        rating.setUserId(3L);

        when(ratingRepository.findAll()).thenReturn(Collections.singletonList(rating));

        // When
        List<RatingWithIdDto> result = ratingRestController.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());

        RatingWithIdDto resultDto = result.get(0);
        assertEquals(rating.getId(), resultDto.getId());
        assertEquals(rating.getStars(), resultDto.getStars());
        assertEquals(rating.getBookId(), resultDto.getBookId());
        assertEquals(rating.getUserId(), resultDto.getUserId());

        verify(ratingRepository).findAll();
    }

    @Test
    public void getOneRating() {
        // Given
        Long ratingId = 1L;
        Rating rating = new Rating();
        rating.setId(ratingId);
        rating.setStars(4D);
        rating.setBookId(2L);
        rating.setUserId(3L);

        when(reviewService.getRating(ratingId)).thenReturn(rating);

        // When
        RatingWithIdDto result = ratingRestController.getOne(ratingId);

        // Then
        assertNotNull(result);
        assertEquals(rating.getId(), result.getId());
        assertEquals(rating.getStars(), result.getStars());
        assertEquals(rating.getBookId(), result.getBookId());
        assertEquals(rating.getUserId(), result.getUserId());

        verify(reviewService).getRating(ratingId);
    }

    @Test
    public void createRating() {
        // Given
        RatingDto ratingDto = new RatingDto();
        ratingDto.setStars(4D);
        ratingDto.setBookId(2L);
        ratingDto.setUserId(3L);

        Rating rating = new Rating();
        rating.setId(1L);
        rating.setStars(4D);
        rating.setBookId(2L);
        rating.setUserId(3L);

        when(reviewService.addRating(anyDouble(), anyLong(), anyLong())).thenReturn(rating);

        // When
        ResponseEntity<RatingWithIdDto> result = ratingRestController.createRating(ratingDto);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        RatingWithIdDto resultDto = result.getBody();
        assertNotNull(resultDto);
        assertEquals(rating.getId(), resultDto.getId());
        assertEquals(rating.getStars(), resultDto.getStars());
        assertEquals(rating.getBookId(), resultDto.getBookId());
        assertEquals(rating.getUserId(), resultDto.getUserId());

        verify(reviewService).addRating(eq(4D), eq(2L), eq(3L));
    }

    @Test
    public void updateRating() {
        // Given
        Long ratingId = 1L;
        RatingDto ratingDto = new RatingDto();
        ratingDto.setStars(3D);
        ratingDto.setBookId(2L);
        ratingDto.setUserId(3L);

        Rating existingRating = new Rating();
        existingRating.setId(ratingId);
        existingRating.setStars(4D);
        existingRating.setBookId(2L);
        existingRating.setUserId(3L);

        when(reviewService.getRating(ratingId)).thenReturn(existingRating);

        // When
        ResponseEntity<RatingWithIdDto> result = ratingRestController.updateRating(ratingId, ratingDto);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        RatingWithIdDto resultDto = result.getBody();
        assertNotNull(resultDto);
        assertEquals(ratingId, resultDto.getId());
        assertEquals(ratingDto.getStars(), resultDto.getStars());
        assertEquals(ratingDto.getBookId(), resultDto.getBookId());
        assertEquals(ratingDto.getUserId(), resultDto.getUserId());

        verify(reviewService).getRating(ratingId);
    }

    @Test
    public void deleteRating() {
        // Given
        Long ratingId = 1L;

        // When
        ResponseEntity<Void> result = ratingRestController.deleteRating(ratingId);

        // Then
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(ratingRepository).deleteById(ratingId);
    }
}
