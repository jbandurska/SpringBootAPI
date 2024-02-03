package project.goodreads.processors;

import lombok.Data;

@Data
public class SearchProcessor {

    private String jpaQuery = "";

    public void build(String key, String operation, String value) {
        System.out.println(key + " " + operation + " " + value);
        if (jpaQuery.length() > 0) {
            jpaQuery += " AND ";
        }

        jpaQuery += toKeyQuery(key) + " " + toOperationQuery(operation) + " " + toValueQuery(value);
    }

    private String toKeyQuery(String key) {
        return "LOWER(t." + key + ")";
    }

    private String toOperationQuery(String operation) {
        return "LIKE";
    }

    private String toValueQuery(String value) {
        return "LOWER('%" + value + "%')";
    }
}

// @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%',
// :key, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :key, '%'))")