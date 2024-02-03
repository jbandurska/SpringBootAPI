package project.goodreads.repositories;

import java.util.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class CustomQueryRepositoryImpl<T> implements CustomQueryRepository<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<T> findWithCustomQuery(String query, Class<T> resultType) {
        System.out.println("\u001B[32m" + query + "\u001B[0m");

        List<?> list = entityManager.createQuery(query).getResultList();
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
    public List<T> findAll(Class<T> resultType) {

        return findWithCustomQuery("SELECT t FROM " + resultType.getSimpleName() + " t",
                resultType);
    }

}
