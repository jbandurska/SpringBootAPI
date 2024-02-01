package project.goodreads.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.goodreads.enums.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithIdDto {

    @NotNull
    private Long id;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    private Role role;

}
