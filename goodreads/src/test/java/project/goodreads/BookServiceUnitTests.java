package project.goodreads;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.goodreads.models.Book;
import project.goodreads.repositories.BookRepository;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.repositories.CommentRepository;
import project.goodreads.repositories.RatingRepository;
import project.goodreads.services.BookService;

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookshelfRepository bookshelfRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    public void getBook() {
        // Given
        Long bookId = 1L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        Book result = bookService.getBook(bookId);

        // Then
        verify(bookRepository).findById(bookId);
        assert result == book;
    }

    @Test
    public void getBookNotFound() {
        // Given
        Long bookId = 2L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookService.getBook(bookId));
        verify(bookRepository).findById(bookId);
    }

    @Test
    public void deleteBook() {
        // Given
        Long bookId = 3L;
        Book book = new Book();
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        bookService.deleteBook(bookId);

        // Then
        verify(bookRepository).findById(bookId);
        verify(ratingRepository).deleteAllByBookId(bookId);
        verify(commentRepository).deleteAllByBookId(bookId);
        verify(bookshelfRepository).saveAll(Collections.emptyList());
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    public void deleteBookNotFound() {
        // Given
        Long bookId = 4L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository).findById(bookId);
    }

}
