package com.example.BE_mini_project.authentication.dto;

public class RegistrationDTO {
    private String username;
    private String email;
    private String password;
    private String referralCode;


    public RegistrationDTO() {
        super();
    }

    public RegistrationDTO(String username, String email, String password, String referralCode) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.referralCode = referralCode;
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

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String toString() {
        return "Registration info: username: " + this.username + " email: " + this.email + " password: " + this.password + "referralCode: " + this.referralCode;
    }
}
