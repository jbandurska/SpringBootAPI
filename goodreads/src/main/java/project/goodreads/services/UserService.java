package project.goodreads.services;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import project.goodreads.exceptions.NullException;
import project.goodreads.exceptions.UserAlreadyExistException;
import project.goodreads.models.User;
import project.goodreads.repositories.BookshelfRepository;
import project.goodreads.repositories.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;
    final private BookshelfService bookshelfService;
    final private BookshelfRepository bookshelfRepository;

    public User getUser(Long id) {
        if (id == null)
            throw new NullException("User id cannot be null");

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return user;
    }

    public User createUser(String username) {

        if (usernameExist(username)) {
            throw new UserAlreadyExistException("User with this username already exists: " + username);
        }

        User user = new User();
        user.setUsername(username);

        User savedUser = userRepository.save(user);

        bookshelfService.createBookshelf("read", savedUser);
        bookshelfService.createBookshelf("to be read", savedUser);

        return user;
    }

    private boolean usernameExist(String username) {

        return userRepository.findByUsername(username).isPresent();
    }

    @SuppressWarnings("null")
    public User updateUser(User user, String username) {

        if (username != null && !username.isEmpty())
            user.setUsername(username);

        userRepository.save(user);

        return user;
    }

    public void deleteUser(User user) {

        deleteUser(user.getId());
    }

    public void deleteUser(Long id) {
        if (id == null)
            throw new NullException("User id cannot be null");

        bookshelfRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }
}
