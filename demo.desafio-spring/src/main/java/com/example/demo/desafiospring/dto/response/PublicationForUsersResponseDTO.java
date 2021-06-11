package com.example.demo.desafiospring.dto.response;

import com.example.demo.desafiospring.dto.request.PublicationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublicationForUsersResponseDTO {
    private int userId;
    private List<PublicationRequestDTO> posts;
}
