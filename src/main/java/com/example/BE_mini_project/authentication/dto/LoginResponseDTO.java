package com.example.BE_mini_project.authentication.dto;

import com.example.BE_mini_project.authentication.model.Users;

public class LoginResponseDTO {
    private Users user;
    private String jwt;

    public LoginResponseDTO(){
        super();
    }

    public LoginResponseDTO(Users user, String jwt){
        this.user = user;
        this.jwt = jwt;
    }

    public Users getUser(){
        return this.user;
    }

    public void setUser(Users user){
        this.user = user;
    }

    public String getJwt(){
        return this.jwt;
    }

    public void setJwt(String jwt){
        this.jwt = jwt;
    }

}