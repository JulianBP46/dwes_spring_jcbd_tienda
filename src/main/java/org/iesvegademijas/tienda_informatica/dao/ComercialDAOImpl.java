package org.iesvegademijas.tienda_informatica.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.iesvegademijas.tienda_informatica.modelo.Comercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ComercialDAOImpl implements ComercialDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Comercial mapRowComercial(ResultSet rs, int rowNum) throws SQLException {
		Float comision = null;
		try {
			Object val = rs.getObject("comisión");
			if (val != null) {
				comision = rs.getFloat("comisión");
			}
		} catch (SQLException e) {
			try {
				Object val = rs.getObject("comision");
				if (val != null) {
					comision = rs.getFloat("comision");
				}
			} catch (SQLException ignored) {
			}
		}

		return new Comercial(
				rs.getInt("id"),
				rs.getString("nombre"),
				rs.getString("apellido1"),
				rs.getString("apellido2"),
				comision
		);
	}

	/**
	 * Inserta en base de datos el nuevo comercial, actualizando el id en el bean comercial.
	 */
	@Override
	public synchronized void create(Comercial comercial) {
		String sqlConAcento = "INSERT INTO comercial (nombre, apellido1, apellido2, comisión) VALUES (?, ?, ?, ?)";
		String sqlSinAcento = "INSERT INTO comercial (nombre, apellido1, apellido2, comision) VALUES (?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		try {
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sqlConAcento, new String[] { "id" });
				int idx = 1;
				ps.setString(idx++, comercial.getNombre());
				ps.setString(idx++, comercial.getApellido1());
				ps.setString(idx++, comercial.getApellido2());
				if (comercial.getComision() != null) {
					ps.setFloat(idx, comercial.getComision());
				} else {
					ps.setNull(idx, java.sql.Types.FLOAT);
				}
				return ps;
			}, keyHolder);
		} catch (BadSqlGrammarException e) {
			jdbcTemplate.update(connection -> {
				PreparedStatement ps = connection.prepareStatement(sqlSinAcento, new String[] { "id" });
				int idx = 1;
				ps.setString(idx++, comercial.getNombre());
				ps.setString(idx++, comercial.getApellido1());
				ps.setString(idx++, comercial.getApellido2());
				if (comercial.getComision() != null) {
					ps.setFloat(idx, comercial.getComision());
				} else {
					ps.setNull(idx, java.sql.Types.FLOAT);
				}
				return ps;
			}, keyHolder);
		}

		if (keyHolder.getKey() != null) {
			comercial.setId(keyHolder.getKey().intValue());
		}
	}

	/**
	 * Devuelve lista con todos los comerciales.
	 */
	@Override
	public List<Comercial> getAll() {
		return jdbcTemplate.query("SELECT * FROM comercial", this::mapRowComercial);
	}

	/**
	 * Devuelve Optional de comercial con el ID dado.
	 */
	@Override
	public Optional<Comercial> find(int id) {
		try {
			Comercial comercial = jdbcTemplate.queryForObject(
					"SELECT * FROM comercial WHERE id = ?",
					this::mapRowComercial,
					id
			);
			return Optional.ofNullable(comercial);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	/**
	 * Actualiza comercial con campos del bean comercial según ID del mismo.
	 */
	@Override
	public void update(Comercial comercial) {
		int rows;
		try {
			rows = jdbcTemplate.update(
					"UPDATE comercial SET nombre = ?, apellido1 = ?, apellido2 = ?, comisión = ? WHERE id = ?",
					comercial.getNombre(),
					comercial.getApellido1(),
					comercial.getApellido2(),
					comercial.getComision(),
					comercial.getId()
			);
		} catch (BadSqlGrammarException e) {
			rows = jdbcTemplate.update(
					"UPDATE comercial SET nombre = ?, apellido1 = ?, apellido2 = ?, comision = ? WHERE id = ?",
					comercial.getNombre(),
					comercial.getApellido1(),
					comercial.getApellido2(),
					comercial.getComision(),
					comercial.getId()
			);
		}

		if (rows == 0) {
			System.out.println("Update de comercial con 0 registros actualizados.");
		}
	}

	/**
	 * Borra comercial con ID proporcionado.
	 */
	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);
		if (rows == 0) {
			System.out.println("Delete de comercial con 0 registros actualizados.");
		}
	}
}

