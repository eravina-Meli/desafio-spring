package com.example.demo.desafiospring.repository;

import com.example.demo.desafiospring.dto.request.PublicationRequestDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersResponseDTO;
import com.example.demo.desafiospring.models.Publication;
import org.springframework.http.ResponseEntity;

public interface IRepositoryProduct {
    ResponseEntity<PublicationRequestDTO> newPost(Publication publication);

    ResponseEntity<PublicationForUsersResponseDTO> getPublicationForUsers(int userId,String date);
}
