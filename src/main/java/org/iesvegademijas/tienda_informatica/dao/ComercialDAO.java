package org.iesvegademijas.tienda_informatica.dao;

import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Comercial;

public interface ComercialDAO {

	public void create(Comercial comercial);

	public List<Comercial> getAll();

	public Optional<Comercial> find(int id);

	public void update(Comercial comercial);

	public void delete(int id);

}

