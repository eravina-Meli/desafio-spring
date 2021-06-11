package com.example.demo.desafiospring.models;

import lombok.*;


//Usuario Comprador
@Getter
@Setter
public class UserBuyer extends Users{

    public UserBuyer(int userId, String userName, String whatIs) {
        super(userId, userName, whatIs);
    }

    public UserBuyer() {
    }
}
