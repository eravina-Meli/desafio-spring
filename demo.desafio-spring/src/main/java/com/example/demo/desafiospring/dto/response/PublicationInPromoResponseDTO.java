package com.example.demo.desafiospring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicationInPromoResponseDTO {
    private int userId;
    private String userName;
    private int promoproducts_count;
}
