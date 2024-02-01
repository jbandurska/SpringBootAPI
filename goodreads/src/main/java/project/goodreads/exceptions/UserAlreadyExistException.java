package project.goodreads.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserAlreadyExistException extends RuntimeException {

    private String message;
}
