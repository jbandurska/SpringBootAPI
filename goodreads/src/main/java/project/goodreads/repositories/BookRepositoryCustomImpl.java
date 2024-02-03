package project.goodreads.repositories;

import java.util.*;

import org.hibernate.TypeMismatchException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import project.goodreads.models.Book;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Book> findWithCustomQuery(String query) {
        List<?> list = entityManager.createQuery(query).getResultList();
        List<Book> books = new ArrayList<Book>();

        for (var item : list) {
            if (item instanceof Book bookItem)
                books.add(bookItem);
            else
                throw new TypeMismatchException("Query should return only objects with class Book");
        }

        return books;
    }

}
