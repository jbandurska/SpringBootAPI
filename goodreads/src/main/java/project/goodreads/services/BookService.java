package project.goodreads.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.exceptions.NullException;
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

    public Book getBook(Long id) {
        if (id == null)
            throw new NullException("Book id cannot be null");

        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        return book;
    }

    public void getAllBooks(Map<String, String> allRequestParams) {
        System.out.println(allRequestParams);

        for (Map.Entry<String, String> entry : allRequestParams.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void deleteBook(Long id) {
        if (id == null)
            throw new NullException("Book id cannot be null");

        deleteRatingsAboutBook(id);
        deleteCommentsAboutBook(id);
        deleteBookFromBookshelves(id);

        bookRepository.deleteById(id);
    }

    public void deleteRatingsAboutBook(Long bookId) {
        ratingRepository.deleteAllByBookId(bookId);
    }

    public void deleteCommentsAboutBook(Long bookId) {
        commentRepository.deleteAllByBookId(bookId);
    }

    @SuppressWarnings("null")
    public void deleteBookFromBookshelves(Long bookId) {
        var book = getBook(bookId);

        List<Bookshelf> modifiedBookshelves = book.getOnShelf().stream()
                .peek(bookshelf -> bookshelf.getBooks().remove(book))
                .collect(Collectors.toList());

        bookshelfRepository.saveAll(modifiedBookshelves);
    }
}
