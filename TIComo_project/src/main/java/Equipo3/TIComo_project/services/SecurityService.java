package Equipo3.TIComo_project.services;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.dao.UserRepository;
import Equipo3.TIComo_project.model.User;


@Service
public class SecurityService {
	
	@Autowired
	private UserRepository userDAO;
	
	public String[] comprobarPassword(JSONObject info) {

		String[] list = new String[2];

		String pwd1 = info.getString("pwd1");
		String pwd2 = info.getString("pwd2");
		list[0] = "false";
		if (!pwd1.equals(pwd2)) {
			list[1] = "Las contraseñas tienen que ser iguales";
		}
		else {
			if(pwd1.length() < 8) {
				list[1] = "La contraseña debe tener al menos 8 caracteres";
			}
			else if (!pwd1.matches(".*[0-9].*")) {
				list[1] = "La contraseña debe contener al menos un digito";
			}
			else if (pwd1.equals(pwd1.toLowerCase())) {
				list[1] = "La contraseña debe tener al menos una mayúscula";
			}
			else if (pwd1.equals(pwd1.toUpperCase())) {
				list[1] = "La contraseña debe tener al menos una minúscula";
			}
			else {
				list[0] = "true";
				list[1] = "OK";
			}
		}
		return list;
	}
	
	public boolean accesoAdmin(HttpSession session) {
		Object admino = session.getAttribute("rol");
		if (admino != null) {
			String admin = admino.toString();
			if (admin.equals("admin")) {
				String correo = session.getAttribute("correo").toString();
				String password = session.getAttribute("password").toString();
				if(this.credenciales(correo, password))
					return true;
			}
		}
		return false;	
	}
	
	public boolean accesoCliente(HttpSession session) {
		Object cliente = session.getAttribute("rol");
		if (cliente != null) {
			String client = cliente.toString();
			if (client.equals("client")) {
				String correo = session.getAttribute("correo").toString();
				String password = session.getAttribute("password").toString();
				if(this.credenciales(correo, password))
					return true;
			}
		}
		return false;	
	}

	private boolean credenciales(String correo, String password) {
		User user = this.userDAO.findByCorreo(correo);
		if (user != null) {
			String pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(password);
			if (user.getPassword().equals(pwd))
				return true;
		}
		return false;
	}
	
	
}