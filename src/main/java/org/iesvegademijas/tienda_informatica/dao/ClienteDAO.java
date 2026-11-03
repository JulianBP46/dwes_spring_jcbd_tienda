package org.iesvegademijas.tienda_informatica.dao;

import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Cliente;

public interface ClienteDAO {

	public void create(Cliente cliente);

	public List<Cliente> getAll();

	public Optional<Cliente> find(int id);

	public void update(Cliente cliente);

	public void delete(int id);

}

