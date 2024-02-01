package project.goodreads.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.goodreads.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :key, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :key, '%'))")
    public Set<Book> findBooksByKey(@Param("key") String key);

}
