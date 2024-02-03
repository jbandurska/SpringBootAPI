package project.goodreads.processors;

import lombok.Data;
import java.util.*;

@Data
public class SearchProcessor {

    private String jpaQuery = "";
    private final List<String> nonStringFields = List.of("yearOfRelease");

    public void build(String key, String operation, String value) {
        System.out.println(key + " " + operation + " " + value);
        if (jpaQuery.length() > 0) {
            jpaQuery += " AND ";
        }

        var nonString = nonStringFields.contains(key);
        jpaQuery += toKeyQuery(key, nonString) + " " + toOperationQuery(operation, nonString) + " "
                + toValueQuery(value, nonString);
    }

    private String toKeyQuery(String key, boolean nonString) {
        if (nonString)
            return key;

        return "LOWER(t." + key + ")";
    }

    private String toOperationQuery(String operation, boolean nonString) {
        if (operation.equals(":") && nonString)
            return "=";

        if (operation.equals(":"))
            return "LIKE";

        return operation;
    }

    private String toValueQuery(String value, boolean nonString) {
        if (nonString)
            return value;

        return "LOWER('%" + value + "%')";
    }
}
