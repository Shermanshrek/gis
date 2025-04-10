package ru.gisback.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gisback.model.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
