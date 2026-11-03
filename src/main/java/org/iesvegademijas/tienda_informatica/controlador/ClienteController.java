package org.iesvegademijas.tienda_informatica.controlador;

import java.util.List;

import org.iesvegademijas.tienda_informatica.modelo.Cliente;
import org.iesvegademijas.tienda_informatica.servicio.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@GetMapping("/clientes")
	public String listar(Model model) {
		List<Cliente> listAllCli = clienteService.listAll();
		model.addAttribute("listaClientes", listAllCli);
		return "clientes";
	}

	@GetMapping("/clientes/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		Cliente cliente = clienteService.one(id);
		model.addAttribute("cliente", cliente);
		return "detalle-cliente";
	}

	@GetMapping("/clientes/crear")
	public String crear(Model model) {
		Cliente cliente = new Cliente();
		model.addAttribute("cliente", cliente);
		return "crear-cliente";
	}

	@PostMapping("/clientes/crear")
	public RedirectView submitCrear(@ModelAttribute("cliente") Cliente cliente) {
		clienteService.newCliente(cliente);
		return new RedirectView("/clientes");
	}

	@GetMapping("/clientes/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		Cliente cliente = clienteService.one(id);
		model.addAttribute("cliente", cliente);
		return "editar-cliente";
	}

	@PostMapping("/clientes/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("cliente") Cliente cliente) {
		clienteService.replaceCliente(cliente);
		return new RedirectView("/clientes");
	}

	@PostMapping("/clientes/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		clienteService.deleteCliente(id);
		return new RedirectView("/clientes");
	}

}

