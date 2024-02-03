package project.goodreads.repositories;

import java.util.List;

import project.goodreads.models.Book;

public interface BookRepositoryCustom {
    List<Book> findWithCustomQuery(String query);
}
