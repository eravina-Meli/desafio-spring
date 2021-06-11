package com.example.demo.desafiospring.service.imple;

import com.example.demo.desafiospring.dto.response.FollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UnFollowSellerResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersCountResponseDTO;
import com.example.demo.desafiospring.dto.response.UserFollowersListResponseDTO;
import com.example.demo.desafiospring.models.UserBuyer;
import com.example.demo.desafiospring.models.UserSeller;
import com.example.demo.desafiospring.models.Users;
import com.example.demo.desafiospring.repository.IRepositoryUser;
import com.example.demo.desafiospring.service.IServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ServiceUserImpl implements IServiceUser {

    //Inyección de dependencia del Repositorio.
    @Autowired
    private IRepositoryUser iRepositoryUsers;

    //Metodo del servicio que llama a repository para registrar que el usuario comprador siga al vendedor.
    @Override
    public ResponseEntity<FollowSellerResponseDTO> followSellerService(int userId, int userIdToFollow) throws RuntimeException{
        FollowSellerResponseDTO followSellerDTO = new FollowSellerResponseDTO();
        Users user1 = null;
        Users user2 = null;

        if(userId == userIdToFollow){
            throw new RuntimeException("El usuário " + userId + " no se puede seguir así mismo");
        }
        if(userId > 0 && userIdToFollow > 0) {
            //Si el usuário userId si no es seguidor del userIdToFollow, va a poder seguirlo, en cambio, ya lo sigue actualmente
            if (!isFollower(userId, userIdToFollow)) {
                //Verificamos qué usuario sigue a quien:
                user1 = isSellerOrBuy(userId);
                user2 = isSellerOrBuy(userIdToFollow);

                if(user1 == null || user2 == null){
                    throw new RuntimeException("Error - Verifique que los usuários ingresados existan en el repository (Archivo JSON)");
                }
                if((user1 instanceof UserBuyer && user2 instanceof UserBuyer) ||
                        (user1 instanceof UserSeller && user2 instanceof UserBuyer)){
                    throw new RuntimeException("Solamente pueden seguirse entre Vendedor -> Vendedor o Comprador -> Vendedor.");
                }
                followSellerDTO.setMessage("El Usuário " + userId + " comenzó a seguir al usuário " + userIdToFollow);
                return iRepositoryUsers.addFollowSellerRepository(user1, user2);
            } else {
                followSellerDTO.setMessage("El Usuario " + userId + " ya seguía actualmente al usuário " + userIdToFollow);
                return new ResponseEntity<>(followSellerDTO, HttpStatus.OK);
            }
        }else{
            followSellerDTO.setMessage("Los Usuários " + 0 + " no existen.");
           return new ResponseEntity<>(followSellerDTO,HttpStatus.BAD_REQUEST);
        }
    }

    public void addListUserFollower(Users userSeller,Users user){
        iRepositoryUsers.addListFollowers(userSeller, user);
    }

     //Método que va a checkear que el usuario comprador sigue o no al vendedor.
    public boolean isFollower(int userBuyer, int userIdToFollow){
        boolean follower = iRepositoryUsers.isFollower(userBuyer,userIdToFollow);
        return follower;
    }

    //Verificamos si el usuario que nos pasan sea comprador o vendedor
    public Users isSellerOrBuy(int userId){
        Users user = iRepositoryUsers.isSellerOrBuy(userId);
        return user;
    }

    //Método que va a llamar al repository para obtener la cantidad de usuarios que siguen a un determinado vendedor
    @Override
    public ResponseEntity<UserFollowersCountResponseDTO> getFollowersByUserId(int userId) throws RuntimeException{
        UserFollowersCountResponseDTO usr= new UserFollowersCountResponseDTO();
        if(userId > 0){
            Users user = iRepositoryUsers.isSellerOrBuy(userId);
                if (user instanceof UserSeller) {
                    return iRepositoryUsers.getFollowersByUserId(user);
                } else {
                    throw new RuntimeException("El usuário que está consultando es un usuário comprador, debe consultar un Vendedor.");
                }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Metodo que va a retornar todos los usuarios que siguen a 1 vendedor especifico.
    @Override
    public ResponseEntity<UserFollowersListResponseDTO> getListFollowersByUserId(int userId, String name) {
        UserFollowersListResponseDTO usr= new UserFollowersListResponseDTO();
        if(userId > 0){
            Users user = iRepositoryUsers.isSellerOrBuy(userId);
            if (user instanceof UserSeller) {
                return iRepositoryUsers.getListFollowersByUserId(user,name);
            } else {
                throw new RuntimeException("El usuário que está consultando es un usuário comprador, debe consultar un Vendedor.");
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Método que va a retormar listado de la personas que yo sigo.
    @Override
    public ResponseEntity<UserFollowersListResponseDTO> getListFollowedByUserId(int userId,String order) {
        UserFollowersListResponseDTO usr= new UserFollowersListResponseDTO();
        if(userId > 0){
            Users user = iRepositoryUsers.isSellerOrBuy(userId);
            if (user instanceof UserSeller) {
                return iRepositoryUsers.getListFollowedByUserId(user,order);
            } else {
                throw new RuntimeException("El usuário que está consultando es un usuário comprador, debe consultar un Vendedor.");
            }
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //Método que va a permitir dejar de seguir a un usuario.
    @Override
    public ResponseEntity<UnFollowSellerResponseDTO> unFollowSellerService(int userId, int userIdToFollow) {

        if (userId == userIdToFollow) {
            throw new RuntimeException("El usuário " + userId + " no se puede seguir así mismo");
        }
        if (userId > 0 && userIdToFollow > 0) {
            if (isFollower(userId, userIdToFollow)) {
                return iRepositoryUsers.UnFollowSellerRepository(userId, userIdToFollow);
            }
            else{
                throw new RuntimeException("El usuário " + userId + " dejo de seguir al usuário " + userIdToFollow);
            }
        }
        throw new RuntimeException("Error - Los usuários no pueden tener Id 0 o un número negativo:");
    }
}
