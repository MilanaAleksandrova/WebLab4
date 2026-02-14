package ru.itmo.web.lab4.dto;

public class AuthResponse {
    private Long id;
    private String username;

    public AuthResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
