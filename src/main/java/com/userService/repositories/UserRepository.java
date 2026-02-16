package com.userService.repositories;

import com.userService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Override
    Optional<User> findById(Long aLong);

    @Override
    User save(User entity);

    Optional<User> findByEmail(String email);
}
