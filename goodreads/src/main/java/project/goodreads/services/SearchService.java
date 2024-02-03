package project.goodreads.services;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.models.Book;
import project.goodreads.processors.SearchProcessor;
import project.goodreads.repositories.BookRepository;

import java.util.regex.*;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchService {

    private final BookRepository bookRepository;

    public List<Book> getBooks(String searchString) {
        if (searchString == null || searchString.length() == 0) {
            return bookRepository.findAll();
        }

        return bookRepository.findWithCustomQuery(toJpaQuery(searchString));
    }

    private String toJpaQuery(String searchString) {
        var searchProcessor = new SearchProcessor();

        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(searchString + ",");
        while (matcher.find()) {
            searchProcessor.build(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        return "SELECT b FROM Book b WHERE " + searchProcessor.getJpaQuery();
    }

}
