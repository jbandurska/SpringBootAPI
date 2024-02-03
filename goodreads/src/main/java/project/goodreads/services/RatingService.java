package project.goodreads.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.exceptions.NullException;
import project.goodreads.models.Rating;
import project.goodreads.repositories.RatingRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserService userService;
    private final BookService bookService;

    public Rating getRating(Long id) {
        if (id == null)
            throw new NullException("Rating id cannot be null");

        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rating not found"));

        return rating;
    }

    public Rating addRating(Double stars, Long bookId, Long userId) {
        var rating = new Rating();

        return updateRating(rating, stars, bookId, userId);
    }

    public Rating updateRating(Rating rating, Double stars, Long bookId, Long userId) {
        rating.setStars(stars);
        rating.setBook(bookService.getBook(bookId));
        rating.setUser(userService.getUser(userId));

        ratingRepository.save(rating);

        return rating;
    }

    public void deleteRating(Long id) {
        if (id == null)
            throw new NullException("Rating id cannot be null");

        ratingRepository.deleteById(id);
    }
}
