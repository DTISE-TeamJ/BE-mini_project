package com.example.BE_mini_project.authentication.dto;

public class RegistrationDTO {
    private String username;
    private String email;
    private String password;

    public RegistrationDTO() {
        super();
    }

    public RegistrationDTO(String username, String email, String password) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "Registration info: username: " + this.username + " email: " + this.email + " password: " + this.password;
    }
}
