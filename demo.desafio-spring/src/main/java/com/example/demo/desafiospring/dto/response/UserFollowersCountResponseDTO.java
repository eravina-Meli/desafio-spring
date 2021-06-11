package com.example.demo.desafiospring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFollowersCountResponseDTO {
    private String userId;
    private String userName;
    private long followers_count = 0;
}
