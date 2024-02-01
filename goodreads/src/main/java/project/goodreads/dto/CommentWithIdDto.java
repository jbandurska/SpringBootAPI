package project.goodreads.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentWithIdDto {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String content;

    @NotNull
    private Long bookId;

    @NotNull
    @NotEmpty
    private Long userId;

}
