package project.goodreads.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.exceptions.NullException;
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
        Bookshelf bookshelf = new Bookshelf();

        bookshelf.setName(name);
        bookshelf.setUser(user);

        bookshelfRepository.save(bookshelf);

        return bookshelf;
    }

    public void addBookToBookshelf(Long bookshelfId, Long bookId) {
        if (bookshelfId == null)
            throw new NullException("Bookshelf id cannot be null");
        if (bookId == null)
            throw new NullException("Book id cannot be null");

        Bookshelf bookshelf = bookshelfRepository.findById(bookshelfId)
                .orElseThrow(() -> new EntityNotFoundException("Bookshelf with id " + bookshelfId + " not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found."));

        bookshelf.getBooks().add(book);
        bookshelfRepository.save(bookshelf);
    }

    public void deleteBookFromBookshelf(Long bookshelfId, Long bookId) {
        if (bookshelfId == null)
            throw new NullException("Bookshelf id cannot be null");
        if (bookId == null)
            throw new NullException("Book id cannot be null");

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

    public Bookshelf getBookshelf(Long bookshelfId) {
        if (bookshelfId == null)
            throw new NullException("Bookshelf id cannot be null");

        return bookshelfRepository.findById(bookshelfId)
                .orElseThrow(() -> new EntityNotFoundException("Bookshelf with id " + bookshelfId + " not found."));
    }

    public void deleteBookshelf(Long id) {
        if (id == null)
            throw new NullException("Bookshelf id cannot be null");

        bookshelfRepository.deleteById(id);
    }

}
