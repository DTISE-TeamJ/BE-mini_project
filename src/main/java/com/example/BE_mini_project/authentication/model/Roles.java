package com.example.BE_mini_project.authentication.model;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;

@Entity
@Table(name="roles")
public class Roles implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    private String authority;

    public Roles(){
        super();
    }

    public Roles(String authority){
        this.authority = authority;
    }

    public Roles(Integer id, String authority){
        this.id = id;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority){
        this.authority = authority;
    }

    public Integer getRoleId(){
        return this.id;
    }

    public void setRoleId(Integer roleId){
        this.id = roleId;
    }
}