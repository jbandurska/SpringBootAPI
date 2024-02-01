package project.goodreads.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.enums.Role;
import project.goodreads.exceptions.UserAlreadyExistException;
import project.goodreads.models.User;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;
    final private PasswordEncoder passwordEncoder;
    final private BookshelfService bookshelfService;
    final private BookshelfRepository bookshelfRepository;

    public User getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user;
    }

    public User createUser(String username, String password, Role role) {

        if (usernameExist(username)) {
            throw new UserAlreadyExistException("User with this username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        userRepository.save(user);

        bookshelfService.createBookshelf("read", user, true);
        bookshelfService.createBookshelf("to be read", user, true);

        return user;
    }

    public User createUser(String username, String password) {

        return createUser(username, password, Role.USER);
    }

    private boolean usernameExist(String username) {

        return userRepository.findByUsername(username).isPresent();
    }

    public User updateUser(User user, String username, String password) {

        if (username != null && !username.isEmpty())
            user.setUsername(username);
        if (password != null && !password.isEmpty())
            user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);

        return user;
    }

    public User updateUser(User user, String username, String password, Role role) {

        user.setRole(role);

        return updateUser(user, username, password);
    }

    public void deleteUser(User user) {

        deleteUserById(user.getId());
    }

    public void deleteUserById(Long id) {

        bookshelfRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }
}
