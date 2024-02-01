package project.goodreads.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.goodreads.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
