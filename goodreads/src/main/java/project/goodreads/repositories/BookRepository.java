package project.goodreads.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.goodreads.models.Book;

public interface BookRepository
        extends JpaRepository<Book, Long>, CustomQueryRepository<Book> {
}
