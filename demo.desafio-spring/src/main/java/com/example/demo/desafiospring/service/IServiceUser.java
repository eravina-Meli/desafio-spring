package com.example.demo.desafiospring.service;

import com.example.demo.desafiospring.dto.response.FollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UnFollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersCountResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersListResponseDTO;
import org.springframework.http.ResponseEntity;

public interface IServiceUser {
    ResponseEntity<FollowSellerResponseDTO> followSellerService(int userId, int userIdToFollow);

    ResponseEntity<UserFollowersCountResponseDTO> getFollowersByUserId(int userId);

    ResponseEntity<UserFollowersListResponseDTO> getListFollowersByUserId(int userId,String name);

    ResponseEntity<UserFollowersListResponseDTO> getListFollowedByUserId(int userId,String order);

    ResponseEntity<UnFollowSellerResponseDTO> unFollowSellerService(int userId, int userIdToFollow);

}
