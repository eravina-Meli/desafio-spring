package com.example.demo.desafiospring.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private int userId;
    private String userName;
    private String whatIs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return userId == users.userId;
    }
}
