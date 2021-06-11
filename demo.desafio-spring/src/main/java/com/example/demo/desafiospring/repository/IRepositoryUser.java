package com.example.demo.desafiospring.repository;

import com.example.demo.desafiospring.dto.response.FollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UnFollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersCountResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersListResponseDTO;
import com.example.demo.desafiospring.models.Users;
import org.springframework.http.ResponseEntity;

public interface IRepositoryUser {
    ResponseEntity<FollowSellerResponseDTO> addFollowSellerRepository(Users userId, Users userIdToFollow);

    boolean isFollower(int userBuyer, int userSeller);

    ResponseEntity<UserFollowersCountResponseDTO> getFollowersByUserId(Users user) throws RuntimeException;

    Users isSellerOrBuy(int userId);

    void addListFollowers(Users userseller, Users user);

    ResponseEntity<UserFollowersListResponseDTO> getListFollowersByUserId(Users user,String name);

    ResponseEntity<UserFollowersListResponseDTO> getListFollowedByUserId(Users userId,String order);

    ResponseEntity<UnFollowSellerResponseDTO> UnFollowSellerRepository(int user1, int user2);
}
