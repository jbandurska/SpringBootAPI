package project.goodreads.repositories;

import java.util.*;

import org.springframework.data.domain.PageRequest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

public class CustomQueryRepositoryImpl<T> implements CustomQueryRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<T> findWithCustomQuery(String queryString, Class<T> resultType, PageRequest pageRequest) {

        Query query = entityManager.createQuery(queryString);

        query.setFirstResult(pageRequest.getPageNumber() * pageRequest.getPageSize());
        query.setMaxResults(pageRequest.getPageSize());

        List<?> list = query.getResultList();
        List<T> result = new ArrayList<>();

        for (var item : list) {
            if (resultType.isInstance(item))
                result.add(resultType.cast(item));
            else
                throw new ClassCastException(
                        "Query should return only objects with class " + resultType.getSimpleName());
        }

        return result;
    }

    @Override
    public List<T> findAll(Class<T> resultType, PageRequest pageRequest) {

        return findWithCustomQuery("SELECT t FROM " + resultType.getSimpleName() + " t",
                resultType, pageRequest);
    }

}
