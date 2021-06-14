package com.example.demo.desafiospring.service.imple;

import com.example.demo.desafiospring.dto.request.PublicationRequestDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersInPromotionResponseDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersResponseDTO;
import com.example.demo.desafiospring.dto.response.PublicationInPromoResponseDTO;
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

    //Damos de alta una nueva publicación con descuento.
    @Override
    public ResponseEntity<PublicationRequestDTO> newPromoPost(Publication publication) {
        return iRepositoryProduct.newPromoPost(publication);
    }

    //Obtenemos las publicaciones que estan en promoción de unn determinado vendedor.
    @Override
    public ResponseEntity<PublicationInPromoResponseDTO> getCountPublicationsInPromo(int userId) {
        return iRepositoryProduct.getCountPublicationsInPromo(userId);
    }

    @Override
    public ResponseEntity<PublicationForUsersInPromotionResponseDTO> getListPublicacionsInPromo(int userId) {
        return iRepositoryProduct.getListPublicationsInPromo(userId);
    }
}
