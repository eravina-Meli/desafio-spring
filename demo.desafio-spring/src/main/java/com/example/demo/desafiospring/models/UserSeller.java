package com.example.demo.desafiospring.models;

import lombok.*;

import java.util.List;

//Usuario Vendedor
@Getter
@Setter
public class UserSeller extends Users{

    private List<Users> followers;

    public UserSeller(int userId, String userName, String whatIs, List<Users> followers) {
        super(userId, userName, whatIs);

        this.followers = followers;
    }

    public UserSeller() {
    }


    public int getCountFollowersUsers(){
        return followers.size();
    }

    public void addFollowers(Users user){
        followers.add(user);
    }

}
