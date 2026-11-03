package org.iesvegademijas.tienda_informatica.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ClienteDAOImpl implements ClienteDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Cliente mapRowCliente(ResultSet rs, int rowNum) throws SQLException {
		Integer categoria = null;
		try {
			Object val = rs.getObject("categoría");
			if (val != null) {
				categoria = rs.getInt("categoría");
			}
		} catch (SQLException e) {
			try {
				Object val = rs.getObject("categoria");
				if (val != null) {
					categoria = rs.getInt("categoria");
				}
			} catch (SQLException ignored) {
			}
		}

		return new Cliente(
				rs.getInt("id"),
				rs.getString("nombre"),
				rs.getString("apellido1"),
				rs.getString("apellido2"),
				rs.getString("ciudad"),
				categoria
		);
	}

	/**
	 * Inserta en base de datos el nuevo cliente, actualizando el id en el bean cliente.
	 */
	@Override
	public synchronized void create(Cliente cliente) {
		String sqlConAcento = "INSERT INTO cliente (nombre, apellido1, apellido2, ciudad, categoría) VALUES (?, ?, ?, ?, ?)";
		String sqlSinAcento = "INSERT INTO cliente (nombre, apellido1, apellido2, ciudad, categoria) VALUES (?, ?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sqlConAcento, new String[] { "id" });
				int idx = 1;
				ps.setString(idx++, cliente.getNombre());
				ps.setString(idx++, cliente.getApellido1());
				ps.setString(idx++, cliente.getApellido2());
				ps.setString(idx++, cliente.getCiudad());
				if (cliente.getCategoria() != null) {
					ps.setInt(idx, cliente.getCategoria());
				} else {
					ps.setNull(idx, java.sql.Types.INTEGER);
				}
				return ps;
			}, keyHolder);
		} catch (BadSqlGrammarException e) {
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sqlSinAcento, new String[] { "id" });
				int idx = 1;
				ps.setString(idx++, cliente.getNombre());
				ps.setString(idx++, cliente.getApellido1());
				ps.setString(idx++, cliente.getApellido2());
				ps.setString(idx++, cliente.getCiudad());
				if (cliente.getCategoria() != null) {
					ps.setInt(idx, cliente.getCategoria());
				} else {
					ps.setNull(idx, java.sql.Types.INTEGER);
				}
				return ps;
			}, keyHolder);
		}

		if (keyHolder.getKey() != null) {
			cliente.setId(keyHolder.getKey().intValue());
		}
	}

	/**
	 * Devuelve lista con todos los clientes.
	 */
	@Override
	public List<Cliente> getAll() {
		return jdbcTemplate.query("SELECT * FROM cliente", this::mapRowCliente);
	}

	/**
	 * Devuelve Optional de cliente con el ID dado.
	 */
	@Override
	public Optional<Cliente> find(int id) {
		try {
			Cliente cliente = jdbcTemplate.queryForObject(
					"SELECT * FROM cliente WHERE id = ?",
					this::mapRowCliente,
					id
			);
			return Optional.ofNullable(cliente);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	/**
	 * Actualiza cliente con campos del bean cliente según ID del mismo.
	 */
	@Override
	public void update(Cliente cliente) {
		int rows;
		try {
			rows = jdbcTemplate.update(
					"UPDATE cliente SET nombre = ?, apellido1 = ?, apellido2 = ?, ciudad = ?, categoría = ? WHERE id = ?",
					cliente.getNombre(),
					cliente.getApellido1(),
					cliente.getApellido2(),
					cliente.getCiudad(),
					cliente.getCategoria(),
					cliente.getId()
			);
		} catch (BadSqlGrammarException e) {
			rows = jdbcTemplate.update(
					"UPDATE cliente SET nombre = ?, apellido1 = ?, apellido2 = ?, ciudad = ?, categoria = ? WHERE id = ?",
					cliente.getNombre(),
					cliente.getApellido1(),
					cliente.getApellido2(),
					cliente.getCiudad(),
					cliente.getCategoria(),
					cliente.getId()
			);
		}

		if (rows == 0) {
			System.out.println("Update de cliente con 0 registros actualizados.");
		}
	}

	/**
	 * Borra cliente con ID proporcionado.
	 */
	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM cliente WHERE id = ?", id);
		if (rows == 0) {
			System.out.println("Delete de cliente con 0 registros actualizados.");
		}
	}
}

