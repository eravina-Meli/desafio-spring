package com.example.demo.desafiospring.controller;

import com.example.demo.desafiospring.dto.request.PublicationRequestDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersInPromotionResponseDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersResponseDTO;
import com.example.demo.desafiospring.dto.response.PublicationInPromoResponseDTO;
import com.example.demo.desafiospring.models.Publication;
import com.example.demo.desafiospring.service.IServiceProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ControllerProduct {

    @Autowired
    private IServiceProduct iServiceProduct;

    //Método que va a permitir realizar una publicación.
    @PostMapping("/products/newpost")
    public ResponseEntity<PublicationRequestDTO> newPost(@RequestBody Publication publication){
        return iServiceProduct.newPost(publication);
    }

    //Método que va a obtener un listado de las publicaciones que haya realizado un usuario. El mismo se va a poder ordenar por fecha ASC y DESC mediante
    //el parámetro RequestParam que le pasamos.
    @GetMapping("/products/followed/{userId}/list")
    public ResponseEntity<PublicationForUsersResponseDTO> getPublicationForUsers(@PathVariable int userId, @RequestParam(defaultValue = "") String order){
        return iServiceProduct.getPublicationForUsers(userId,order);
    }

    //Método que va a permitir realizar una publicación en promocion.
    @PostMapping("/products/newpromopost")
    public ResponseEntity<PublicationRequestDTO> newPromoPost(@RequestBody Publication publication){
        return iServiceProduct.newPromoPost(publication);
    }

    //Obtener la cantidad de productos en promoción de un determinado vendedor.
    @GetMapping("/products/{userId}/countPromo")
    public ResponseEntity<PublicationInPromoResponseDTO> getCountPublicationsInPromo(@PathVariable int userId){
        return iServiceProduct.getCountPublicationsInPromo(userId);
    }

    //Obtener un listado de todos los productos en promocion de un dereminado vendedor
    @GetMapping("/products/{userId}/list")
    public ResponseEntity<PublicationForUsersInPromotionResponseDTO> getListPublicacionsInPromo(@PathVariable int userId){
        return iServiceProduct.getListPublicacionsInPromo(userId);
    }
}
