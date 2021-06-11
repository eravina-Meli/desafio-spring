package com.example.demo.desafiospring.dto.request;

import com.example.demo.desafiospring.models.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicationRequestDTO {
    private int userId;
    private int id_post;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private Date date;
    private Product detail;
    private int category;
    private double price;
}
