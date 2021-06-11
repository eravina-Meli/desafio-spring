package com.example.demo.desafiospring.repository.imple;

import com.example.demo.desafiospring.dto.response.*;
import com.example.demo.desafiospring.models.UserBuyer;
import com.example.demo.desafiospring.models.UserSeller;
import com.example.demo.desafiospring.models.Users;
import com.example.demo.desafiospring.repository.IRepositoryUser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Repository
public class RepositoryUserImpl implements IRepositoryUser {

    //Lista que se carga con los usuarios que están en el archivo json.
    private List<Users> users;

    //Método que se va a encargar de cargar el archivo con todos los Usuario y retornarlo.
    private List<Users> loadUsers() {

        List<Users> ing = null;
        File user = null;

        try {
            user = ResourceUtils.getFile("classpath:users.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        var objectMapper = new ObjectMapper();

        try {
            ing = objectMapper.readValue(user, new TypeReference<List<Users>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ing;
    }

    //Lista de usuarios que que siguen a otros usuarios
    private HashMap<Integer, List<Users>> mapListUsers;

    //Constructor que va a contener el hashmap con la info de los usuario, y cargamos todos los usuarios a parte.
    public RepositoryUserImpl(HashMap<Integer, List<Users>> mapListUsers){
        this.users = loadUsers();
        this.mapListUsers= mapListUsers;

    }

    //Método que va a permitir agregar a la lista los compradores que siguen a los vendedores.
    @Override
    public ResponseEntity<FollowSellerResponseDTO> addFollowSellerRepository(Users userId, Users userIdToFollow) throws RuntimeException {
        FollowSellerResponseDTO follower = new FollowSellerResponseDTO();
        List<Users> listFollowerSeller = new ArrayList<>();

        //Controlamos que el usrio en el hasmap no sea null, y creamos una nueva lista para poder almacenar sus seguidores.
        if(this.mapListUsers.get(userId) == null || this.mapListUsers.get(userId.getUserId()) == null){
            this.mapListUsers.put(userId.getUserId(),new ArrayList<>());
        }
        //Agregamos a la lista los seguidores
        listFollowerSeller.add(userIdToFollow);
        //Agregamos al hasmap, el usuario que sigue y a quién sigue.
        this.mapListUsers.get(userId.getUserId()).add(userIdToFollow);

        follower.setMessage("El usuário " + userId.getUserId() + " comenzó a seguir al usuário: " + userIdToFollow.getUserId());
        return new ResponseEntity<FollowSellerResponseDTO>(follower,HttpStatus.OK);
    }

    //Método que busca en el HashMap si los usuarios se siguen previamente, en caso de que se siguan, retorna true.
    @Override
    public boolean isFollower(int userBuyer, int userSeller) {
        //Recorremos el Hashmap que contiene para cada usuario, su lista de los que sigue.
        for (Map.Entry<Integer, List<Users>> entry : this.mapListUsers.entrySet()){ //mapFollowers
            if(entry.getKey() == userBuyer){
                for (Users seller : entry.getValue()){
                    if(seller.getUserId() == userSeller){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Método que va a sumar la cantidad de seguidores que siguen a un determinado vendedor
    @Override
    public ResponseEntity<UserFollowersCountResponseDTO> getFollowersByUserId(Users user) throws RuntimeException {
        if(user instanceof UserSeller) {
            //Creamos un UserFollowersCountDTO y le cargamos la info necesaria.
            UserFollowersCountResponseDTO userFollowersCountResponseDTO = new UserFollowersCountResponseDTO();
            userFollowersCountResponseDTO.setUserId(Integer.toString(user.getUserId()));
            userFollowersCountResponseDTO.setUserName(user.getUserName());
            long countFollows = 0;

            //Recorremos el hashmap de usuarios y vamos contabilizando sus seguidores.
            for (Map.Entry<Integer, List<Users>> entry : this.mapListUsers.entrySet()){
                for (Users u : entry.getValue()){
                    if(u.getUserId() == user.getUserId()){
                        countFollows++;
                    }
                }
            }
            //seteamos la cantidad de seguidores para el usuario a retornar.
            userFollowersCountResponseDTO.setFollowers_count(countFollows);
            return new ResponseEntity<UserFollowersCountResponseDTO>(userFollowersCountResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Método que va a devolver un usuario comprador o vendedor, dependiendo del atributo getWhatIs que viene en el archivo json.
    @Override
    public Users isSellerOrBuy(int userId) {
        Users usr = null;
        for (Users user : this.users){
            if(user.getUserId() == userId){
                if(user.getWhatIs().equals("BUY")){
                    usr = new UserBuyer();
                    usr.setUserId(user.getUserId());
                    usr.setUserName(user.getUserName());
                    usr.setWhatIs("BUY");
                }else{
                    usr = new UserSeller();
                    usr.setUserId(user.getUserId());
                    usr.setUserName(user.getUserName());
                    usr.setWhatIs("SELLER");

                }
            }
        }
        return usr;
    }

    //Método que agrega los usuarios Compradores que sigue a 1 vendedor.
    @Override
    public void addListFollowers(Users u ,Users user) {
        List<Users> listFollowerSeller = new ArrayList<>();
        listFollowerSeller.add(user);
        this.mapListUsers.put(u.getUserId(), listFollowerSeller);
    }

    //Método que va a devolver todos los usuarios que siguen a 1 vendedor,
    @Override
    public ResponseEntity<UserFollowersListResponseDTO> getListFollowersByUserId(Users user, String name) throws RuntimeException{
        if(user instanceof UserSeller) {
            //Seteamos algunos datos a mostrar del DTO.
            UserFollowersListResponseDTO userFollowersListResponseDTO = new UserFollowersListResponseDTO();
            userFollowersListResponseDTO.setUserId(user.getUserId());
            userFollowersListResponseDTO.setUserName(user.getUserName());
            Users isSellerOrBuy;
            UserResponseDTO userResponseDTO;

            //Recorremos la lista, y por cada seguidor, seteamos en el DTO el id y el nombre, luego lo agregamos a la lista de
            //usuarios.
            for (Map.Entry<Integer, List<Users>> entry : this.mapListUsers.entrySet()){
                for (Users u : entry.getValue()){
                    if(u.getUserId() == user.getUserId()){
                        isSellerOrBuy = isSellerOrBuy(entry.getKey());
                        userResponseDTO = new UserResponseDTO(isSellerOrBuy.getUserId(),isSellerOrBuy.getUserName());
                        userFollowersListResponseDTO.addUserListDTO(userResponseDTO);
                    }
                }
            }
            //Ordenamos de forma ascendente o descendente, depndiendo de qué nos viene por parámetro (name)
            List<UserResponseDTO> getFollowersDTO = userFollowersListResponseDTO.getFollowers();
            if(name.equals("name_asc")) {
                getFollowersDTO.sort((name1, name2) -> name1.getUserName().compareTo(name2.getUserName()));
            }else {
                getFollowersDTO.sort((name1, name2) -> name2.getUserName().compareTo(name1.getUserName()));
            }
            return new ResponseEntity<UserFollowersListResponseDTO>(userFollowersListResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Método que va a devolver los usuarios a los cuales el mismo user sigue.
    @Override
    public ResponseEntity<UserFollowersListResponseDTO> getListFollowedByUserId(Users user,String name) throws RuntimeException{
        UserFollowersListResponseDTO userFollowersListResponseDTO = new UserFollowersListResponseDTO();
        userFollowersListResponseDTO.setUserId(user.getUserId());
        userFollowersListResponseDTO.setUserName(user.getUserName());

        UserResponseDTO userResponseDTO = null;

        //Recorremos la lista de usuários, los cuales 1 usuario sigue, lo cargamos a la lista y lo retornamos
        for (Map.Entry<Integer, List<Users>> entry : this.mapListUsers.entrySet()){
            if(entry.getKey() == user.getUserId()){
                for (Users u : entry.getValue()){
                    userResponseDTO = new UserResponseDTO(u.getUserId(),u.getUserName());
                    userFollowersListResponseDTO.addUserListDTO(userResponseDTO);
                }
            }
        }
        //Ordenamos de forma ascendente o descendente, depndiendo de qué nos viene por parámetro (name)
        List<UserResponseDTO> getFollowersDTO = userFollowersListResponseDTO.getFollowers();
        if(name.equals("name_asc")) {
            getFollowersDTO.sort((name1, name2) -> name1.getUserName().compareTo(name2.getUserName()));
        }else {
            getFollowersDTO.sort((name1, name2) -> name2.getUserName().compareTo(name1.getUserName()));
        }
        return new ResponseEntity<UserFollowersListResponseDTO>(userFollowersListResponseDTO, HttpStatus.OK);
    }

    //Método que va a permitir poder realizar la acción de “Unfollow” (dejar de seguir) a un determinado vendedor.
    @Override
    public ResponseEntity<UnFollowSellerResponseDTO> UnFollowSellerRepository(int user1, int user2) throws RuntimeException {
        //OBtenemos los seguidores de la lista, y removemos al que pasamos por parametro.
       List <Users> users = this.mapListUsers.get(user1);
       users.removeIf(users1 -> users1.getUserId() == user2);

        UnFollowSellerResponseDTO unFollowSellerResponseDTO = new UnFollowSellerResponseDTO
                ("El usuário " + Integer.toString(user1) + " dejó de seguir al usuário Vendedor " + Integer.toString(user2));
        return new ResponseEntity<UnFollowSellerResponseDTO>(unFollowSellerResponseDTO,HttpStatus.OK);
    }



}
