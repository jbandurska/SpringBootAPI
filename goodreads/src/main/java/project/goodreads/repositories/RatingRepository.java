package project.goodreads.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.goodreads.models.Rating;

@Transactional
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT ROUND(AVG(r.stars), 2) FROM Rating r WHERE r.bookId = :bookId")
    Double getAverageRatingByBookId(@Param("bookId") Long bookId);

    @Query("DELETE FROM Rating r WHERE r.bookId = :bookId")
    @Modifying
    public void deleteAllByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.bookId = :bookId")
    public Long countByBookId(@Param("bookId") Long bookId);

}
