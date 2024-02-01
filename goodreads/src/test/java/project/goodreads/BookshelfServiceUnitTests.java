package project.goodreads;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import project.goodreads.models.Book;
import project.goodreads.models.Bookshelf;
import project.goodreads.models.User;
import project.goodreads.repositories.BookRepository;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.services.BookshelfService;

@ExtendWith(MockitoExtension.class)
public class BookshelfServiceUnitTests {

    @Mock
    private BookshelfRepository bookshelfRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookshelfService bookshelfService;

    @Test
    public void createBookshelf() {
        // Given
        String name = "MyBookshelf";
        User user = new User();

        // When
        Bookshelf createdBookshelf = bookshelfService.createBookshelf(name, user);

        // Then
        assertEquals(name, createdBookshelf.getName());
        assertEquals(user, createdBookshelf.getUser());
        assertEquals(false, createdBookshelf.isHidden());

        verify(bookshelfRepository).save(any(Bookshelf.class));
    }

    @Test
    public void createHiddenBookshelf() {
        // Given
        String name = "HiddenBookshelf";
        User user = new User();

        // When
        Bookshelf createdBookshelf = bookshelfService.createBookshelf(name, user, true);

        // Then
        assertEquals(name, createdBookshelf.getName());
        assertEquals(user, createdBookshelf.getUser());
        assertEquals(true, createdBookshelf.isHidden());

        verify(bookshelfRepository).save(any(Bookshelf.class));
    }

    @Test
    public void addBookToBookshelf() {
        // Given
        Long bookshelfId = 1L;
        Long bookId = 2L;

        Bookshelf bookshelf = new Bookshelf();
        Book book = new Book();

        given(bookshelfRepository.findById(bookshelfId)).willReturn(Optional.of(bookshelf));
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        // When
        bookshelfService.addBookToBookshelf(bookshelfId, bookId);

        // Then
        verify(bookshelfRepository).save(eq(bookshelf));
        assertEquals(1, bookshelf.getBooks().size());
        assertEquals(book, bookshelf.getBooks().iterator().next());
    }

    @Test
    public void deleteBookFromBookshelf() {
        // Given
        Long bookshelfId = 3L;
        Long bookId = 4L;

        Bookshelf bookshelf = new Bookshelf();
        Book book = new Book();
        bookshelf.getBooks().add(book);

        given(bookshelfRepository.findById(bookshelfId)).willReturn(Optional.of(bookshelf));
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        // When
        bookshelfService.deleteBookFromBookshelf(bookshelfId, bookId);

        // Then
        verify(bookshelfRepository).save(eq(bookshelf));
        assertEquals(0, bookshelf.getBooks().size());
    }

    @Test
    public void getBookshelves() {
        // Given
        Long userId = 5L;
        Set<Bookshelf> expectedBookshelves = new HashSet<>();

        given(bookshelfRepository.findAllBookshelvesByUserId(userId)).willReturn(expectedBookshelves);

        // When
        Set<Bookshelf> actualBookshelves = bookshelfService.getBookshelves(userId);

        // Then
        assertEquals(expectedBookshelves, actualBookshelves);
    }

    @Test
    public void getBookshelvesWithoutBook() {
        // Given
        Long userId = 6L;
        Long bookId = 7L;
        Set<Bookshelf> allBookshelves = new HashSet<>();

        given(bookshelfRepository.findAllBookshelvesByUserId(userId)).willReturn(allBookshelves);

        Book book = new Book();
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        // When
        Set<Bookshelf> bookshelvesWithoutBook = bookshelfService.getBookshelvesWithoutBook(userId, bookId);

        // Then
        assertEquals(allBookshelves, bookshelvesWithoutBook);
    }

    @Test
    public void getBookshelfById() {
        // Given
        Long bookshelfId = 8L;
        Bookshelf expectedBookshelf = new Bookshelf();

        given(bookshelfRepository.findById(bookshelfId)).willReturn(Optional.of(expectedBookshelf));

        // When
        Bookshelf actualBookshelf = bookshelfService.getBookshelfById(bookshelfId);

        // Then
        assertEquals(expectedBookshelf, actualBookshelf);
    }

    @Test
    public void getBookshelfByIdNotFound() {
        // Given
        Long bookshelfId = 9L;

        given(bookshelfRepository.findById(bookshelfId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> bookshelfService.getBookshelfById(bookshelfId));
    }
}
