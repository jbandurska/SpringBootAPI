package project.goodreads.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @NotNull
    @NotEmpty
    @NotBlank
    private String content;

    @NotNull
    private Long bookId;

    @NotNull
    private Long userId;

}
