package project.goodreads.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NullException extends RuntimeException {

    private String message;
}
