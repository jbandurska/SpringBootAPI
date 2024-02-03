package project.goodreads.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import project.goodreads.dto.BookshelfWithIdDto;

@Entity
@Getter
@Setter
public class Bookshelf {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_bookshelf", joinColumns = @JoinColumn(name = "bookshelf_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books = new HashSet<>();

    public static BookshelfWithIdDto toBookshelfWithIdDto(Bookshelf bookshelf) {
        var bookshelfDto = new BookshelfWithIdDto();

        bookshelfDto.setId(bookshelf.getId());
        bookshelfDto.setName(bookshelf.getName());
        bookshelfDto.setUserId(bookshelf.getUser().getId());

        return bookshelfDto;
    }

}
