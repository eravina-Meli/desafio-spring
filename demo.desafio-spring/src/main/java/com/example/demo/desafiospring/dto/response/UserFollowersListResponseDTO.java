package com.example.demo.desafiospring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserFollowersListResponseDTO {
    private int userId;
    private String userName;
    private List<UserResponseDTO> followers = new ArrayList<>();

    public void addUserListDTO(UserResponseDTO user){
        this.followers.add(user);
    }

    public List<UserResponseDTO> getFollowers(){
        return this.followers;
    }
}
