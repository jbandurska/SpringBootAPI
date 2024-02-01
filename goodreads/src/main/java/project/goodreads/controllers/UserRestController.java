package project.goodreads.controllers.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import project.goodreads.dto.CreateBookshelfDto;
import project.goodreads.dto.BookshelfWithIdDto;
import project.goodreads.dto.UserDto;
import project.goodreads.dto.UserWithIdDto;
import project.goodreads.models.User;
import project.goodreads.repositories.UserRepository;
import project.goodreads.services.BookshelfService;
import project.goodreads.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Transactional
public class UserRestController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BookshelfService bookshelfService;

    @GetMapping
    public List<UserWithIdDto> getAll() {

        List<User> users = userRepository.findAll();
        List<UserWithIdDto> userDtos = users.stream().map(u -> {

            var userDto = new UserWithIdDto();
            BeanUtils.copyProperties(u, userDto);

            return userDto;
        }).toList();

        return userDtos;
    }

    @GetMapping("/{id}")
    public UserWithIdDto getOne(@PathVariable Long id) {

        var user = userService.getUser(id);

        var userDto = new UserWithIdDto();
        BeanUtils.copyProperties(user, userDto);

        return userDto;
    }

    @PostMapping
    public ResponseEntity<UserWithIdDto> createUser(@Valid @RequestBody UserDto userDto) {

        var user = userService.createUser(userDto.getUsername(), userDto.getPassword(), userDto.getRole());

        var response = new UserWithIdDto();
        BeanUtils.copyProperties(user, response);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{id}/bookshelves")
    public ResponseEntity<BookshelfWithIdDto> createBookshelfForUser(@PathVariable Long id,
            @RequestBody CreateBookshelfDto bookshelfDto) {

        var bookshelf = bookshelfService.createBookshelf(bookshelfDto.getName(), userService.getUser(id));

        var response = new BookshelfWithIdDto();
        BeanUtils.copyProperties(bookshelf, response);

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserWithIdDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {

        var user = userService.updateUser(userService.getUser(id), userDto.getUsername(), userDto.getPassword(),
                userDto.getRole());

        var response = new UserWithIdDto();
        BeanUtils.copyProperties(user, response);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        userService.deleteUserById(id);

        return ResponseEntity.noContent().build();
    }
}
