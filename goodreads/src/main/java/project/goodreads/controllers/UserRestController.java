package project.goodreads.controllers;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import project.goodreads.dto.UserDto;
import project.goodreads.dto.UserWithIdDto;
import project.goodreads.models.User;
import project.goodreads.repositories.UserRepository;
import project.goodreads.services.SearchService;
import project.goodreads.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Transactional
public class UserRestController {

    private final UserService userService;
    private final SearchService<User> searchService;

    public UserRestController(UserRepository userRepository, UserService userService) {
        this.userService = userService;
        this.searchService = new SearchService<>(userRepository);
    }

    @GetMapping
    public List<UserWithIdDto> getAll(@RequestParam(required = false) String search,
            @Min(0) @RequestParam(defaultValue = "0") int page) {

        var pageRequest = PageRequest.of(page, 10);
        List<User> users = searchService.getItems(search, User.class, pageRequest);
        List<UserWithIdDto> userDtos = users.stream().map(u -> User.toUserWithIdDto(u)).toList();

        return userDtos;
    }

    @GetMapping("/{id}")
    public UserWithIdDto getOne(@PathVariable Long id) {

        var user = userService.getUser(id);

        return User.toUserWithIdDto(user);
    }

    @PostMapping
    public ResponseEntity<UserWithIdDto> createUser(@Valid @RequestBody UserDto userDto) {

        var user = userService.createUser(userDto.getUsername());

        return ResponseEntity.status(201).body(User.toUserWithIdDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWithIdDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {

        var user = userService.updateUser(userService.getUser(id), userDto.getUsername());

        return ResponseEntity.ok(User.toUserWithIdDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
