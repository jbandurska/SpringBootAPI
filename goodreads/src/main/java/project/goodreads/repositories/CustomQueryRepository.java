package project.goodreads.repositories;

import java.util.List;

import org.springframework.data.domain.PageRequest;

public interface CustomQueryRepository<T> {
    List<T> findWithCustomQuery(String query, Class<T> resultType, PageRequest pageRequest);

    List<T> findAll(Class<T> resultType, PageRequest pageRequest);
}
