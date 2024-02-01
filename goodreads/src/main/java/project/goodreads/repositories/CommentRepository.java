package project.goodreads.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import project.goodreads.models.Comment;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.bookId = :bookId")
    public Set<Comment> findAllByBookId(@Param("bookId") Long bookId);

    @Query("DELETE FROM Comment c WHERE c.bookId = :bookId")
    @Modifying
    public void deleteAllByBookId(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.bookId = :bookId")
    public Long countByBookId(@Param("bookId") Long bookId);

}
