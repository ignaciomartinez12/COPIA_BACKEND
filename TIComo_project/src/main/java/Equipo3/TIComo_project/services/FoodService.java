package Equipo3.TIComo_project.services;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.dao.PlateRepository;
import Equipo3.TIComo_project.dao.RestaurantRepository;
import Equipo3.TIComo_project.model.Restaurant;
import Equipo3.TIComo_project.model.Plate;

@Service
public class FoodService {

	@Autowired
	private RestaurantRepository restDAO;

	@Autowired
	private PlateRepository platoDAO;

	private String nombre = "nombre";

	@Autowired
	private SecurityService secService;
	
	public String crearRestaurante(JSONObject jso) {

		Restaurant resNombre = this.restDAO.findByNombre(jso.getString(this.nombre));
		if (resNombre != null) 
			return this.nombre;
		Restaurant res = new Restaurant();
		res = this.crearRestAux(jso, res);
		res.setNombre(jso.getString(this.nombre));
		this.restDAO.save(res);
		return "Restaurante creado correctamente";
	}

	public String eliminarRestaurante(String nombreRes) {
		Restaurant res = this.restDAO.findByNombre(nombreRes);
		if (res == null) 
			return this.nombre;
		this.platoDAO.deleteByNombreRestaurante(nombreRes);
		this.restDAO.deleteByNombre(nombreRes);
		return "Restaurante eliminado correctamente";
	}

	public String actualizarRestaurante(JSONObject jso) {
		String nombree = jso.getString("nombre");
		Restaurant res = this.restDAO.findByNombre(nombree);
		if (res == null) 
			return this.nombre;
		this.restDAO.deleteByNombre(nombree);
		res = this.crearRestAux(jso, res);
		this.restDAO.save(res);
		return "Restaurante actualizado correctamente";
	}

	public Restaurant crearRestAux(JSONObject jso, Restaurant res) {
		res.setCategoria(jso.getString("categoria"));
		res.setCif(this.secService.encriptar(jso.getString("cif")));
		res.setDireccion(jso.getString("direccion"));
		res.setEmail(jso.getString("email"));
		res.setRazonSocial(jso.getString("razonSocial"));
		res.setTelefono(jso.getString("telefono"));
		return res;
	}

	public String consultarRestaurantes() {
		StringBuilder bld = new StringBuilder();
		List <Restaurant> restaurantes = this.restDAO.findAll();
		if (!restaurantes.isEmpty()) {
			for (int i=0; i<restaurantes.size(); i++) {
				Restaurant res = restaurantes.get(i);
				JSONObject resJSO = res.toJSON();
				String cifEnc = resJSO.getString("cif");
				resJSO.put("cif", this.secService.desencriptar(cifEnc));
				if (i == restaurantes.size() - 1)
					bld.append(resJSO.toString());
				else
					bld.append(resJSO.toString() + ";");
			}
			bld.toString();
		}
		return "";
	}

	public String eliminarCarta(String nombreRes) {
		Restaurant res = this.restDAO.findByNombre(nombreRes);
		if (res == null)
			return this.nombre;
		this.platoDAO.deleteByNombreRestaurante(nombreRes);
		return "Carta eliminada correctamente";

	}

	public String crearPlato(JSONObject jso) {
		String nombrePlato = jso.getString(this.nombre);
		String nombreRestaurante = jso.getString("nombreRestaurante");
		if (this.existePlatoenRestaurante(nombrePlato, nombreRestaurante)) 
			return this.nombre;
		Plate pla = new Plate();
		pla = this.crearPlatoAux(jso, pla);
		pla.setNombreRestaurante(nombreRestaurante);
		platoDAO.save(pla);
		return "Plato creado correctamente";
	}
	
	public boolean existePlatoenRestaurante(String nombreP, String nombreR) {
		List <Plate> platos = this.platoDAO.findByNombreAndNombreRestaurante(nombreP, nombreR);
		return !platos.isEmpty();
			
	}
	
	public Plate crearPlatoAux(JSONObject jso, Plate pla) {
		pla.setAptoVegano(Boolean.valueOf(jso.getString("aptoVegano")));
		pla.setDescripcion(jso.getString("descripcion"));
		pla.setPrecio(jso.getString("precio"));
		pla.setNombre(jso.getString(this.nombre));
		pla.setFoto(jso.getString("foto"));
		return pla;
	}
	
	public String actualizarPlato(JSONObject jso) {
		String nombreViejo = jso.getString("nombreViejo");
		String nombreNuevo = jso.getString(this.nombre);
		String nombreRestaurante = jso.getString("nombreRestaurante");
		List <Plate> plato = this.platoDAO.findByNombreAndNombreRestaurante(nombreViejo, nombreRestaurante);
		if (plato.isEmpty())
			return "noexiste";
		if (this.existePlatoenRestaurante(nombreNuevo, nombreRestaurante)) 
			return this.nombre;
		
		Plate platt = this.crearPlatoAux(jso, plato.get(0));
		
		this.platoDAO.deleteByNombreAndNombreRestaurante(nombreViejo, nombreRestaurante);
		this.platoDAO.save(platt);
		
		return "Plato actualizado correctamente";
	}

	public String platosParaEnviar(String nombreRestaurante) {
		StringBuilder bld = new StringBuilder();
		List<Plate> listaPlatos = this.platoDAO.findByNombreRestaurante(nombreRestaurante);
		if(!listaPlatos.isEmpty()) {
			for (int i = 0; i<listaPlatos.size(); i++) {
				Plate plato= listaPlatos.get(i);
				JSONObject jso = plato.toJSON();
				if (i == listaPlatos.size() - 1)
					bld.append(jso.toString());
				else
					bld.append(jso.toString() + ";");
			}
			return bld.toString();
		}
		return "";
	}

}
