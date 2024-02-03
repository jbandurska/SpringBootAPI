package project.goodreads.repositories;

import java.util.List;

public interface CustomQueryRepository<T> {
    List<T> findWithCustomQuery(String query, Class<T> resultType);

    List<T> findAll(Class<T> resultType);
}
