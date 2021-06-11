package com.example.demo.desafiospring.util;

import java.sql.Timestamp;

public class Time {
    //Método que nos va a permitir obtener la hora actual a utilizar.
    public static Timestamp getTime(){
        return new Timestamp(System.currentTimeMillis());
    }
}
