package project.goodreads.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import project.goodreads.dto.RatingWithIdDto;

@Entity
@Data
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double stars;

    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public static RatingWithIdDto toRatingWithIdDto(Rating rating) {
        var ratingDto = new RatingWithIdDto();

        ratingDto.setId(rating.getId());
        ratingDto.setStars(rating.getStars());
        ratingDto.setBookId(rating.getBook().getId());
        ratingDto.setUserId(rating.getUser().getId());

        return ratingDto;
    }

}
