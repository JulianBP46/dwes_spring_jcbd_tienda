package org.iesvegademijas.tienda_informatica.servicio;

import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.dao.ComercialDAO;
import org.iesvegademijas.tienda_informatica.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	@Autowired
	private ComercialDAO comercialDAO;

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	public Comercial one(Integer id) {
		Optional<Comercial> optCom = comercialDAO.find(id);
		if (optCom.isPresent())
			return optCom.get();
		else
			return null;
	}

	public void newComercial(Comercial comercial) {
		comercialDAO.create(comercial);
	}

	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}

}

