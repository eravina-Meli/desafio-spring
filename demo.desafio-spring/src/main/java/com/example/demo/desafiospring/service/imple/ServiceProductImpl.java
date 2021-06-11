package com.example.demo.desafiospring.service.imple;

import com.example.demo.desafiospring.dto.request.PublicationRequestDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersResponseDTO;
import com.example.demo.desafiospring.models.Publication;
import com.example.demo.desafiospring.repository.IRepositoryProduct;
import com.example.demo.desafiospring.service.IServiceProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ServiceProductImpl implements IServiceProduct {

    @Autowired
    private IRepositoryProduct iRepositoryProduct;

    //Método que va a llamar a persistir en el repository la publicación
    @Override
    public ResponseEntity<PublicationRequestDTO> newPost(Publication publication) {
        return iRepositoryProduct.newPost(publication);
    }

    //Método que llama al repository para obtener la lista de publicaciones de un usuario.
    @Override
    public ResponseEntity<PublicationForUsersResponseDTO> getPublicationForUsers(int userId,String order) {
        return iRepositoryProduct.getPublicationForUsers(userId,order);
    }
}
