package com.example.atlas.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findUsersByEmail(String email);

    @Query("SELECT u.id FROM Users u WHERE u.email = :email")
    Long findUsersIdByEmail(@Param("email") String email);
}
