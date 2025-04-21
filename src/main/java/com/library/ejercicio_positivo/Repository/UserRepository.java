package com.library.ejercicio_positivo.Repository;

import com.library.ejercicio_positivo.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.username = :username", nativeQuery = false)
    Optional<UserEntity> findByUser(@Param("username") String username);
}
