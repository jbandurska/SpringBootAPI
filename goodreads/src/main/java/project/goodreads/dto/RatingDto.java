package project.goodreads.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

    @NotNull
    @Min(value = 1, message = "Stars cannot be smaller than 1")
    @Max(value = 5, message = "Stars cannot be bigger than 5")
    private Double stars;

    @NotNull
    private Long bookId;

    @NotNull
    private Long userId;

}
