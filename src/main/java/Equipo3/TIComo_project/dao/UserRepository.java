package Equipo3.TIComo_project.dao;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Equipo3.TIComo_project.model.User;

@Repository
public interface UserRepository extends MongoRepository <User, String>{

	User findByCorreo(String correo);

	void deleteByCorreo(String correoUsuario);
	
}