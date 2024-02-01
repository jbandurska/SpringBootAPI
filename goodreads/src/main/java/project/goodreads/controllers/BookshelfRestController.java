package project.goodreads.controllers.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.dto.BookshelfWithIdDto;
import project.goodreads.models.Bookshelf;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.services.BookshelfService;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@RestController
@RequestMapping("/api/bookshelves")
@RequiredArgsConstructor
@Transactional
public class BookshelfRestController {

    private final BookshelfService bookshelfService;
    private final BookshelfRepository bookshelfRepository;

    @GetMapping
    public ResponseEntity<List<BookshelfWithIdDto>> getAllBookshelves() {

        List<Bookshelf> bookshelves = bookshelfRepository.findAll();

        List<BookshelfWithIdDto> bookshelfDtos = bookshelves.stream()
                .map(bookshelf -> new BookshelfWithIdDto(bookshelf.getId(), bookshelf.getName(),
                        bookshelf.getUser().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookshelfDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookshelfWithIdDto> getBookshelf(@PathVariable Long id) {

        bookshelfService.getBookshelfById(id);
        Bookshelf bookshelf = bookshelfService.getBookshelfById(id);

        BookshelfWithIdDto bookshelfDto = new BookshelfWithIdDto(bookshelf.getId(), bookshelf.getName(),
                bookshelf.getUser().getId());

        return ResponseEntity.ok(bookshelfDto);
    }

    @PostMapping("/{bookshelfId}/books/{bookId}")
    public ResponseEntity<String> addBookToBookshelf(@PathVariable Long bookshelfId, @PathVariable Long bookId) {

        bookshelfService.addBookToBookshelf(bookshelfId, bookId);

        return ResponseEntity.ok("Book added to bookshelf successfully.");
    }

    @DeleteMapping("/{bookshelfId}/books/{bookId}")
    public ResponseEntity<Void> deleteBookFromBookshelf(@PathVariable Long bookshelfId, @PathVariable Long bookId) {

        bookshelfService.deleteBookFromBookshelf(bookshelfId, bookId);

        return ResponseEntity.noContent().build();
    }

}
