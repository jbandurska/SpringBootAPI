package project.goodreads.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookWithIdDto {

    @NotNull
    private Long id;

    @NotNull(message = "Title cannot be null")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Author cannot be null")
    @NotEmpty(message = "Author cannot be empty")
    private String author;

    @NotNull
    @Min(868)
    @Max(2040)
    private int yearOfRelease;

}
