package org.iesvegademijas.tienda_informatica;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;

import org.iesvegademijas.tienda_informatica.controlador.ComercialController;
import org.iesvegademijas.tienda_informatica.modelo.Comercial;
import org.iesvegademijas.tienda_informatica.servicio.ComercialService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ComercialController.class)
public class ComercialControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ComercialService comercialService;

	@Test
	public void testListar() throws Exception {
		Comercial c1 = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		when(comercialService.listAll()).thenReturn(Arrays.asList(c1));

		mockMvc.perform(get("/comerciales"))
				.andExpect(status().isOk())
				.andExpect(view().name("comerciales"))
				.andExpect(model().attributeExists("listaComerciales"));
	}

	@Test
	public void testDetalle() throws Exception {
		Comercial c1 = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		when(comercialService.one(1)).thenReturn(c1);

		mockMvc.perform(get("/comerciales/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("detalle-comercial"))
				.andExpect(model().attributeExists("comercial"));
	}

	@Test
	public void testCrearGet() throws Exception {
		mockMvc.perform(get("/comerciales/crear"))
				.andExpect(status().isOk())
				.andExpect(view().name("crear-comercial"))
				.andExpect(model().attributeExists("comercial"));
	}

	@Test
	public void testCrearPost() throws Exception {
		mockMvc.perform(post("/comerciales/crear")
				.param("nombre", "Laura")
				.param("apellido1", "García")
				.param("apellido2", "Pérez")
				.param("comision", "0.20"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/comerciales"));

		verify(comercialService).newComercial(any(Comercial.class));
	}

	@Test
	public void testEditarGet() throws Exception {
		Comercial c1 = new Comercial(1, "Daniel", "Sáez", "Vega", 0.15f);
		when(comercialService.one(1)).thenReturn(c1);

		mockMvc.perform(get("/comerciales/editar/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("editar-comercial"))
				.andExpect(model().attributeExists("comercial"));
	}

	@Test
	public void testEditarPost() throws Exception {
		mockMvc.perform(post("/comerciales/editar/1")
				.param("id", "1")
				.param("nombre", "Daniel")
				.param("apellido1", "Sáez")
				.param("apellido2", "Vega")
				.param("comision", "0.18"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/comerciales"));

		verify(comercialService).replaceComercial(any(Comercial.class));
	}

	@Test
	public void testBorrarPost() throws Exception {
		mockMvc.perform(post("/comerciales/borrar/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/comerciales"));

		verify(comercialService).deleteComercial(1);
	}
}

