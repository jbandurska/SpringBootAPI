package project.goodreads.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.models.Book;
import project.goodreads.models.Bookshelf;
import project.goodreads.models.User;
import project.goodreads.repositories.BookRepository;
import project.goodreads.repositories.BookshelfRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class BookshelfService {

    final BookshelfRepository bookshelfRepository;
    final BookRepository bookRepository;

    public Bookshelf createBookshelf(String name, User user) {
        return createBookshelf(name, user, false);
    }

    public Bookshelf createBookshelf(String name, User user, boolean isHidden) {
        Bookshelf bookshelf = new Bookshelf();

        bookshelf.setName(name);
        bookshelf.setUser(user);
        bookshelf.setHidden(isHidden);

        bookshelfRepository.save(bookshelf);

        return bookshelf;
    }

    public void addBookToBookshelf(Long bookshelfId, Long bookId) {
        Bookshelf bookshelf = bookshelfRepository.findById(bookshelfId)
                .orElseThrow(() -> new EntityNotFoundException("Bookshelf with id " + bookshelfId + " not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found."));

        bookshelf.getBooks().add(book);
        bookshelfRepository.save(bookshelf);
    }

    public void deleteBookFromBookshelf(Long bookshelfId, Long bookId) {
        Bookshelf bookshelf = bookshelfRepository.findById(bookshelfId)
                .orElseThrow(() -> new EntityNotFoundException("Bookshelf with id " + bookshelfId + " not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found."));

        bookshelf.getBooks().remove(book);
        bookshelfRepository.save(bookshelf);
    }

    public Set<Bookshelf> getBookshelves(Long userId) {

        return bookshelfRepository.findAllBookshelvesByUserId(userId);
    }

    public Set<Bookshelf> getBookshelvesWithoutBook(Long userId, Long bookId) {
        var bookshelves = bookshelfRepository.findAllBookshelvesByUserId(userId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found."));

        var bookshelvesWithoutBook = new HashSet<Bookshelf>();

        for (Bookshelf bookshelf : bookshelves) {
            if (!isBookOnShelf(bookshelf, book))
                bookshelvesWithoutBook.add(bookshelf);
        }

        return bookshelvesWithoutBook;
    }

    private boolean isBookOnShelf(Bookshelf bookshelf, Book book) {

        return bookshelf.getBooks().contains(book);
    }

    public Bookshelf getBookshelfById(Long bookshelfId) {

        return bookshelfRepository.findById(bookshelfId)
                .orElseThrow(() -> new EntityNotFoundException("Bookshelf with id " + bookshelfId + " not found."));
    }

}
