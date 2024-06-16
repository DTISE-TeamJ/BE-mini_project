package com.example.BE_mini_project.authentication.service;

import com.example.BE_mini_project.authentication.model.Roles;
import com.example.BE_mini_project.authentication.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    @Transactional
    public Roles findOrCreateRole(String authority) {
        Optional<Roles> optionalRole = rolesRepository.findByAuthority(authority);

        return optionalRole.orElseGet(() -> {
            Roles newRole = new Roles(authority);
            return rolesRepository.save(newRole);
        });
    }
}