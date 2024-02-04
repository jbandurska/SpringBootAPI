package project.goodreads.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import project.goodreads.dto.BookshelfDto;
import project.goodreads.dto.BookshelfWithIdDto;
import project.goodreads.models.Bookshelf;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.services.BookshelfService;
import project.goodreads.services.SearchService;
import project.goodreads.services.UserService;

import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/bookshelves")
@Transactional
public class BookshelfRestController {

    private final BookshelfService bookshelfService;
    private final BookshelfRepository bookshelfRepository;
    private final UserService userService;
    private final SearchService<Bookshelf> searchService;

    public BookshelfRestController(BookshelfService bookshelfService, BookshelfRepository bookshelfRepository,
            UserService userService) {
        this.bookshelfService = bookshelfService;
        this.bookshelfRepository = bookshelfRepository;
        this.userService = userService;
        this.searchService = new SearchService<>(bookshelfRepository);
    }

    @GetMapping
    public ResponseEntity<List<BookshelfWithIdDto>> getAll(@RequestParam(required = false) String search,
            @Min(0) @RequestParam(defaultValue = "0") int page) {

        var pageRequest = PageRequest.of(page, 10);
        List<Bookshelf> bookshelves = searchService.getItems(search, Bookshelf.class, pageRequest);
        List<BookshelfWithIdDto> bookshelfDtos = bookshelves.stream()
                .map(bookshelf -> Bookshelf.toBookshelfWithIdDto(bookshelf))
                .collect(Collectors.toList());

        return ResponseEntity.ok(bookshelfDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookshelfWithIdDto> getBookshelf(@PathVariable Long id) {

        Bookshelf bookshelf = bookshelfService.getBookshelf(id);

        BookshelfWithIdDto bookshelfDto = Bookshelf.toBookshelfWithIdDto(bookshelf);

        return ResponseEntity.ok(bookshelfDto);
    }

    @PostMapping
    public ResponseEntity<BookshelfWithIdDto> createBookshelfForUser(@Valid @RequestBody BookshelfDto bookshelfDto) {

        var user = userService.getUser(bookshelfDto.getUserId());
        var bookshelf = bookshelfService.createBookshelf(bookshelfDto.getName(), user);

        return ResponseEntity.status(201).body(Bookshelf.toBookshelfWithIdDto(bookshelf));
    }

    @PostMapping("/{bookshelfId}/books/{bookId}")
    public ResponseEntity<String> addBookToBookshelf(@PathVariable Long bookshelfId, @PathVariable Long bookId) {

        bookshelfService.addBookToBookshelf(bookshelfId, bookId);

        return ResponseEntity.ok("Book added to bookshelf successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookshelfWithIdDto> updateBookshelf(@PathVariable Long id,
            @Valid @RequestBody BookshelfDto bookshelfDto) {

        var bookshelf = bookshelfService.getBookshelf(id);
        bookshelf.setName(bookshelfDto.getName());
        bookshelfRepository.save(bookshelf);

        return ResponseEntity.ok(Bookshelf.toBookshelfWithIdDto(bookshelf));
    }

    @DeleteMapping("/{bookshelfId}/books/{bookId}")
    public ResponseEntity<Void> deleteBookFromBookshelf(@PathVariable Long bookshelfId, @PathVariable Long bookId) {

        bookshelfService.deleteBookFromBookshelf(bookshelfId, bookId);

        return ResponseEntity.noContent().build();
    }

}
