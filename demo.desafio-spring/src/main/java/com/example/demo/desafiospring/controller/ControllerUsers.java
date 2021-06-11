package com.example.demo.desafiospring.controller;

import com.example.demo.desafiospring.dto.response.FollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UnFollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersCountResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersListResponseDTO;
import com.example.demo.desafiospring.service.IServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerUsers {

    //Intección de depenencia hacia el Servicio.
    @Autowired
    private IServiceUser iServiceUser;

    //Método que va permitir seguir a un determinado vendedor.
    @PostMapping("users/{userId}/follow/{userIdToFollow}")
    public ResponseEntity<FollowSellerResponseDTO> followSeller(@PathVariable int userId, @PathVariable int userIdToFollow) throws RuntimeException{
        return iServiceUser.followSellerService(userId, userIdToFollow);
    }

    //Método que va a obtener el resultado de la cantidad de usuarios que siguen a un determinado vendedor.
    @GetMapping("/users/{userId}/followers/count")
    public ResponseEntity<UserFollowersCountResponseDTO> getFollowersByUserId(@PathVariable int userId){
        return iServiceUser.getFollowersByUserId(userId);
    }

    //Método que va a obtener un listado de todos los usuarios que siguen a un determinado vendedor (¿Quién me sigue?)
    //También va a permitir ordenar por nombre, de manera ASC o DESC.
    @GetMapping("/users/{userId}/followers/list")
    public ResponseEntity<UserFollowersListResponseDTO> getListFollowersByUserId(@PathVariable int userId, @RequestParam(defaultValue = "") String name){
        return iServiceUser.getListFollowersByUserId(userId,name);
    }

    //Método que va a obtener los vendedores que sigue un determinado usuário ¿A quién Sigo?.
    @GetMapping("/users/{userId}/followed/list")
    public ResponseEntity<UserFollowersListResponseDTO> getListFollowerByUserId(@PathVariable int userId, @RequestParam(defaultValue = "") String name){
        return iServiceUser.getListFollowedByUserId(userId,name);
    }

    //Método que va a permitir realizar unFollow a un usuario.
    @PostMapping("users/{userId}/unfollow/{userIdToUnFollow}")
    public ResponseEntity<UnFollowSellerResponseDTO> unFollowSeller(@PathVariable int userId,
                                                                  @PathVariable int userIdToUnFollow) throws RuntimeException{
        return iServiceUser.unFollowSellerService(userId, userIdToUnFollow);
    }
}
