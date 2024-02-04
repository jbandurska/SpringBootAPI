package project.goodreads.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.processors.SearchProcessor;
import project.goodreads.repositories.CustomQueryRepository;

import java.util.regex.*;

import org.springframework.data.domain.PageRequest;

import java.util.List;

@Transactional
@RequiredArgsConstructor
public class SearchService<T> {

    private final CustomQueryRepository<T> customQueryRepository;

    public List<T> getItems(String searchString, Class<T> resultType, PageRequest pageRequest) {
        if (searchString == null || searchString.length() == 0) {
            return customQueryRepository.findAll(resultType, pageRequest);
        }

        return customQueryRepository.findWithCustomQuery(toJpaQuery(searchString, resultType), resultType, pageRequest);
    }

    private String toJpaQuery(String searchString, Class<T> resultType) {

        var searchProcessor = new SearchProcessor();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");

        Matcher matcher = pattern.matcher(searchString + ",");
        while (matcher.find()) {
            searchProcessor.build(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        return "SELECT t FROM " + resultType.getSimpleName() + " t WHERE " + searchProcessor.getJpaQuery();
    }

}
