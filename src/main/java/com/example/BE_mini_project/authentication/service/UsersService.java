package com.example.BE_mini_project.authentication.service;

import com.example.BE_mini_project.authentication.model.Roles;
import com.example.BE_mini_project.authentication.model.Users;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.BE_mini_project.authentication.repository.UsersRepository;

@Service
public class UsersService implements UserDetailsService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesService rolesService;

    /*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("In the user details service");

        return usersRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("user is not valid"));
    }
    */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("In the user details service");
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not valid"));
    }

//    @Transactional
//    public Users createUser(String email, String username, String password, String authority) {
//        Roles role = rolesService.findOrCreateRole(authority);
//        Users user = new Users();
//        user.setEmail(email);
//        user.setUsername(username);
//        user.setPassword(encoder.encode(password));
//        user.setRole(role);
//        return usersRepository.save(user);
//    }
}