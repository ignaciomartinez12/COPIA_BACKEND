package Equipo3.TIComo_project.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import Equipo3.TIComo_project.model.Pedido;

public interface PedidoRepository extends MongoRepository <Pedido, String>{

	List<Pedido> findAllByCliente(String cliente);
	List<Pedido> findAllByEstado(int estado);
	List<Pedido> findAllByRider(String rider);
	List<Pedido> findAllByRestaurante(String restaurante);
}