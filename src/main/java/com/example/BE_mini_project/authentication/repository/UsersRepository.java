package com.example.BE_mini_project.authentication.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.BE_mini_project.authentication.model.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);
    Optional<Users> findById(Long id);
    Optional<Users> findByEmail(String email);
    Optional<Users> findByReferralCode(String referralCode);
    @EntityGraph(attributePaths = "authorities")
    Optional<Users> findWithAuthoritiesByUsername(@Param("username") String username);

    @EntityGraph(attributePaths = "authorities")
    @Query("SELECT u FROM Users u JOIN u.authorities r WHERE r.authority = :authority")
    List<Users> findByRoleAuthority(@Param("authority") String authority);
}
