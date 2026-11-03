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

import org.iesvegademijas.tienda_informatica.controlador.ClienteController;
import org.iesvegademijas.tienda_informatica.modelo.Cliente;
import org.iesvegademijas.tienda_informatica.servicio.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ClienteService clienteService;

	@Test
	public void testListar() throws Exception {
		Cliente c1 = new Cliente(1, "Aarón", "Rivero", "Gómez", "Almería", 100);
		when(clienteService.listAll()).thenReturn(Arrays.asList(c1));

		mockMvc.perform(get("/clientes"))
				.andExpect(status().isOk())
				.andExpect(view().name("clientes"))
				.andExpect(model().attributeExists("listaClientes"));
	}

	@Test
	public void testDetalle() throws Exception {
		Cliente c1 = new Cliente(1, "Aarón", "Rivero", "Gómez", "Almería", 100);
		when(clienteService.one(1)).thenReturn(c1);

		mockMvc.perform(get("/clientes/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("detalle-cliente"))
				.andExpect(model().attributeExists("cliente"));
	}

	@Test
	public void testCrearGet() throws Exception {
		mockMvc.perform(get("/clientes/crear"))
				.andExpect(status().isOk())
				.andExpect(view().name("crear-cliente"))
				.andExpect(model().attributeExists("cliente"));
	}

	@Test
	public void testCrearPost() throws Exception {
		mockMvc.perform(post("/clientes/crear")
				.param("nombre", "Laura")
				.param("apellido1", "García")
				.param("apellido2", "Pérez")
				.param("ciudad", "Málaga")
				.param("categoria", "150"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clientes"));

		verify(clienteService).newCliente(any(Cliente.class));
	}

	@Test
	public void testEditarGet() throws Exception {
		Cliente c1 = new Cliente(1, "Aarón", "Rivero", "Gómez", "Almería", 100);
		when(clienteService.one(1)).thenReturn(c1);

		mockMvc.perform(get("/clientes/editar/1"))
				.andExpect(status().isOk())
				.andExpect(view().name("editar-cliente"))
				.andExpect(model().attributeExists("cliente"));
	}

	@Test
	public void testEditarPost() throws Exception {
		mockMvc.perform(post("/clientes/editar/1")
				.param("id", "1")
				.param("nombre", "Aarón")
				.param("apellido1", "Rivero")
				.param("apellido2", "Gómez")
				.param("ciudad", "Granada")
				.param("categoria", "200"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clientes"));

		verify(clienteService).replaceCliente(any(Cliente.class));
	}

	@Test
	public void testBorrarPost() throws Exception {
		mockMvc.perform(post("/clientes/borrar/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/clientes"));

		verify(clienteService).deleteCliente(1);
	}
}

