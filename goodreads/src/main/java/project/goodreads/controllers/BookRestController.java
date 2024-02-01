package project.goodreads.controllers.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.goodreads.dto.BookDto;
import project.goodreads.dto.BookWithIdDto;
import project.goodreads.models.Book;
import project.goodreads.repositories.BookRepository;
import project.goodreads.services.BookService;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Transactional
public class BookRestController {

    private final BookRepository bookRepository;
    private final BookService bookService;

    @GetMapping
    public List<BookWithIdDto> getAll() {

        List<Book> books = bookRepository.findAll();
        List<BookWithIdDto> booksDtos = books.stream().map(b -> {

            var bookDto = new BookWithIdDto();
            BeanUtils.copyProperties(b, bookDto);

            return bookDto;
        }).toList();

        return booksDtos;
    }

    @GetMapping("/{id}")
    public BookWithIdDto getOne(@PathVariable Long id) {

        var book = bookService.getBook(id);

        var bookDto = new BookWithIdDto();
        BeanUtils.copyProperties(book, bookDto);

        return bookDto;
    }

    @PostMapping
    public ResponseEntity<BookWithIdDto> createBook(@Valid @RequestBody BookDto bookDto) {

        var book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        var createdBook = bookRepository.save(book);

        var response = new BookWithIdDto();
        BeanUtils.copyProperties(createdBook, response);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookWithIdDto> updateBook(@PathVariable Long id, @Valid @RequestBody BookDto bookDto) {

        var book = bookService.getBook(id);
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        bookRepository.save(book);

        var response = new BookWithIdDto();
        BeanUtils.copyProperties(book, response);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}
