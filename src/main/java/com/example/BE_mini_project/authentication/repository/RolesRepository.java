package com.example.BE_mini_project.authentication.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.BE_mini_project.authentication.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer>{
    Optional<Roles> findByAuthority(String authority);
}