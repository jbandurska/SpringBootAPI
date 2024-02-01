package project.goodreads.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.models.Book;
import project.goodreads.models.Bookshelf;
import project.goodreads.repositories.BookRepository;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.repositories.CommentRepository;
import project.goodreads.repositories.RatingRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookshelfRepository bookshelfRepository;
    private final RatingRepository ratingRepository;
    private final CommentRepository commentRepository;

    public Book getBook(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        return book;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBook(Long bookId) {

        deleteRatingsAboutBook(bookId);
        deleteCommentsAboutBook(bookId);
        deleteBookFromBookshelves(bookId);

        bookRepository.deleteById(bookId);
    }

    public void deleteRatingsAboutBook(Long bookId) {
        ratingRepository.deleteAllByBookId(bookId);
    }

    public void deleteCommentsAboutBook(Long bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    public void deleteBookFromBookshelves(Long bookId) {
        var book = getBook(bookId);

        List<Bookshelf> modifiedBookshelves = book.getOnShelf().stream()
                .peek(bookshelf -> bookshelf.getBooks().remove(book))
                .collect(Collectors.toList());

        bookshelfRepository.saveAll(modifiedBookshelves);
    }
}
