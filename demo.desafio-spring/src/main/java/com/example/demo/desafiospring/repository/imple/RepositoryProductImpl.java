package com.example.demo.desafiospring.repository.imple;

import com.example.demo.desafiospring.dto.request.PublicationRequestDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersInPromotionResponseDTO;
import com.example.demo.desafiospring.dto.response.PublicationForUsersResponseDTO;
import com.example.demo.desafiospring.dto.response.PublicationInPromoResponseDTO;
import com.example.demo.desafiospring.models.Product;
import com.example.demo.desafiospring.models.Publication;
import com.example.demo.desafiospring.models.Users;
import com.example.demo.desafiospring.repository.IRepositoryProduct;
import com.example.demo.desafiospring.repository.IRepositoryUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RepositoryProductImpl implements IRepositoryProduct {

    private List<Product> products;
    private List<PublicationRequestDTO> publications;

    @Autowired
    private IRepositoryUser iRepositoryUser;

    public RepositoryProductImpl(List<Product> products,List<PublicationRequestDTO> publications){
        this.products = products;
        this.publications = publications;
    }

    //Método que va a verificar que exista el producto de la publicación, en caso de que si, crea la misma.
    @Override
    public ResponseEntity<PublicationRequestDTO> newPost(Publication publication) throws RuntimeException{
        PublicationRequestDTO publicationRequestDTO =  new PublicationRequestDTO();
        Product p = null;
        Publication publication1 = null;

        //Validamos si existe una publicación con el id_post que nos envian.
        for (PublicationRequestDTO pub : this.publications){
            if(pub.getId_post() == publication.getId_post()){
                throw new RuntimeException("Error - Ya existe una publicación con el id_post: " + pub.getId_post());
            }
        }
        //Vamos a buscar el producto que esta en la publicación para ver si existe, si no existe lo creamos.
        for (Product prd : this.products){
            if(prd.getProduct_id() == publication.getDetail().getProduct_id()){
                p = prd;
            }
        }
        if(p == null){
            p = crearProducto(publication);
            //Si tengo producto,creamos la publicacionDTO y la retornamos con el producto.
            publicationRequestDTO.setUserId(publication.getUserId());
            publicationRequestDTO.setId_post(publication.getId_post());
            publicationRequestDTO.setDate(publication.getDate());
            publicationRequestDTO.setDetail(p);
            this.products.add(p);
            publicationRequestDTO.setCategory(publication.getCategory());
            publicationRequestDTO.setPrice(publication.getPrice());
            this.publications.add(publicationRequestDTO);
        }else{
            throw new RuntimeException("Error - El producto existe en otra publicación, no tenemos más stock para el producto: " + p.getProduct_id());
        }
        return new ResponseEntity<PublicationRequestDTO>(publicationRequestDTO, HttpStatus.OK);
    }

    /* Método que va a devolver las publicaciones realizadas por los vendedores que un
      usuário sigue en las últimas 2 semanas (para esto tener en cuenta ordenamiento por
      fecha, publicaciones más recientes primero).
    */
    @Override
    public ResponseEntity<PublicationForUsersResponseDTO> getPublicationForUsers(int userId,String order) {
        List<PublicationRequestDTO> publicationsRequests = new ArrayList<>();
        PublicationRequestDTO newPublicationRequestDTO = null;
        List<PublicationRequestDTO> publicationForUsersResponseDTOS = new ArrayList<>();
        PublicationForUsersResponseDTO objectPublicationForUser = null;
        int compare =0;

        //Obtenemos todas las publicaciones que el usuário realizó.
        publicationsRequests = this.publications
                .stream()
                .filter(p -> p.getUserId() == userId)
                .collect(Collectors.toList());

        //Para cada publicación validamos que la fecha sea en el rango de las últimas 2 semanas.
        for (PublicationRequestDTO p : publicationsRequests){
            compare = p.getDate().compareTo(getDateBeforeTwoWeeks());
            if(compare >= 0){
                newPublicationRequestDTO = new PublicationRequestDTO(
                        p.getUserId(),p.getId_post(),p.getDate(),p.getDetail(),p.getCategory(),p.getPrice(),p.isHasPromo(),p.getDiscount());
                publicationForUsersResponseDTOS.add(newPublicationRequestDTO);
                objectPublicationForUser = new PublicationForUsersResponseDTO(userId,publicationForUsersResponseDTOS);
            }
        }

        //Si recibimos date=asc, ordenamos las fechas de manera ascendente, sino lo hacemos descendente.
        if(order.equals("date_asc")){
            objectPublicationForUser.getPosts().sort((order1,order2) -> order1.getDate().compareTo(order2.getDate()));
            return new ResponseEntity<PublicationForUsersResponseDTO>(objectPublicationForUser,HttpStatus.OK);
        }else{
            objectPublicationForUser.getPosts().sort((order1,order2) -> order2.getDate().compareTo(order1.getDate()));
            return new ResponseEntity<PublicationForUsersResponseDTO>(objectPublicationForUser,HttpStatus.OK);
        }
    }

    //Método que da de alta una publicación con promoción
    @Override
    public ResponseEntity<PublicationRequestDTO> newPromoPost(Publication publication) {
        PublicationRequestDTO publicationRequestDTO =  new PublicationRequestDTO();
        Product p = null;

        //Validamos si existe una publicación con el id_post que nos envian.
        for (PublicationRequestDTO pub : this.publications){
            if(pub.getId_post() == publication.getId_post()){
                throw new RuntimeException("Error - Ya existe una publicación con el id_post: " + pub.getId_post());
            }
        }
        //Vamos a buscar el producto que esta en la publicación para ver si existe, si no existe lo creamos.
        for (Product prd : this.products){
            if(prd.getProduct_id() == publication.getDetail().getProduct_id()){
                p = prd;
            }
        }
        if(p == null){
            p = crearProducto(publication);
            //Si tengo producto,creamos la publicacionDTO y la retornamos con el producto.
            publicationRequestDTO.setUserId(publication.getUserId());
            publicationRequestDTO.setId_post(publication.getId_post());
            publicationRequestDTO.setDate(publication.getDate());
            publicationRequestDTO.setDetail(p);
            this.products.add(p);
            publicationRequestDTO.setCategory(publication.getCategory());
            publicationRequestDTO.setPrice(publication.getPrice());
            publicationRequestDTO.setHasPromo(publication.isHasPromo());
            publicationRequestDTO.setDiscount(publication.getDiscount());
            this.publications.add(publicationRequestDTO);
        }else{
            throw new RuntimeException("Error - El producto existe en otra publicación, no tenemos más stock para el producto: " + p.getProduct_id());
        }
        return new ResponseEntity<PublicationRequestDTO>(publicationRequestDTO, HttpStatus.OK);
    }

    //Método que va a retornar la cantidad de productos en promoción de un determinado vendedor.
    @Override
    public ResponseEntity<PublicationInPromoResponseDTO> getCountPublicationsInPromo(int userId) throws RuntimeException{
        PublicationInPromoResponseDTO resultPublicacionPromoResponseDTO= null;
        Users users = iRepositoryUser.isSellerOrBuy(userId);
        int countPromo =0;
        if(users != null) {
            //obtenmos la publicaciones que realizó el Usuario
            for (PublicationRequestDTO p : this.publications) {
                if (p.isHasPromo() && p.getUserId() == users.getUserId()) {
                    countPromo++;
                }
            }
            resultPublicacionPromoResponseDTO = new PublicationInPromoResponseDTO(users.getUserId(),users.getUserName(),countPromo);
        }
        return new ResponseEntity<>(resultPublicacionPromoResponseDTO, HttpStatus.OK);
    }

    //Método que va a retornar una lista de publicaciones en promoción de un usuario especifico.
    @Override
    public ResponseEntity<PublicationForUsersInPromotionResponseDTO> getListPublicationsInPromo(int userId) {
        //Creamos las instancias que vamos a necesitar para retornar el DTO con sus datos.
        PublicationForUsersInPromotionResponseDTO publicationForUsersInPromotionResponseDTO = null;
        List<PublicationRequestDTO> listPublicationUser = new ArrayList<>();
        //Vamos a buscar el usuario que estamos pasando por parámetro
        Users users = iRepositoryUser.isSellerOrBuy(userId);
        if(users != null) {
            //Obtenemos las publicaciones en promoción del usuário
            for (PublicationRequestDTO p : this.publications) {
                if (p.isHasPromo() && p.getUserId() == users.getUserId()) {
                    //Cada publicación en promoción, lo agregamos al a lista.
                    listPublicationUser.add(p);
                }
            }
            //Creamos el DTO a mostrar
            publicationForUsersInPromotionResponseDTO = new PublicationForUsersInPromotionResponseDTO(
                    users.getUserId(),users.getUserName(),listPublicationUser);
        }

        return new ResponseEntity<>(publicationForUsersInPromotionResponseDTO,HttpStatus.OK);
    }

    //Método que va a retornar las últimas 2 semanas, para controlar la publicaciones.
    public Date getDateBeforeTwoWeeks() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -14);
        return calendar.getTime();
    }

    //Método que va a crear un producto cuando se realice el newPost.
    public Product crearProducto(Publication publication){
        //Creamos el producto
        Product p = new Product();
        p.setProduct_id(publication.getDetail().getProduct_id());
        p.setProduct_name(publication.getDetail().getProduct_name());
        p.setType(publication.getDetail().getType());
        p.setBrand(publication.getDetail().getBrand());
        p.setColor(publication.getDetail().getColor());
        p.setNotes(publication.getDetail().getNotes());
        return p;
    }


}
