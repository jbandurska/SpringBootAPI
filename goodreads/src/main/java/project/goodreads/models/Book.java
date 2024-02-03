package project.goodreads.models;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;
import project.goodreads.dto.BookWithIdDto;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String author;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Bookshelf> onShelf = new HashSet<>();

    public static BookWithIdDto toBookWithIdDto(Book book) {
        var bookDto = new BookWithIdDto();

        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());

        return bookDto;
    }

}
