package org.iesvegademijas.tienda_informatica.controlador;

import java.util.List;

import org.iesvegademijas.tienda_informatica.modelo.Comercial;
import org.iesvegademijas.tienda_informatica.servicio.ComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ComercialController {

	@Autowired
	private ComercialService comercialService;

	@GetMapping("/comerciales")
	public String listar(Model model) {
		List<Comercial> listAllCom = comercialService.listAll();
		model.addAttribute("listaComerciales", listAllCom);
		return "comerciales";
	}

	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		Comercial comercial = comercialService.one(id);
		model.addAttribute("comercial", comercial);
		return "detalle-comercial";
	}

	@GetMapping("/comerciales/crear")
	public String crear(Model model) {
		Comercial comercial = new Comercial();
		model.addAttribute("comercial", comercial);
		return "crear-comercial";
	}

	@PostMapping("/comerciales/crear")
	public RedirectView submitCrear(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.newComercial(comercial);
		return new RedirectView("/comerciales");
	}

	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		Comercial comercial = comercialService.one(id);
		model.addAttribute("comercial", comercial);
		return "editar-comercial";
	}

	@PostMapping("/comerciales/editar/{id}")
	public RedirectView submitEditar(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.replaceComercial(comercial);
		return new RedirectView("/comerciales");
	}

	@PostMapping("/comerciales/borrar/{id}")
	public RedirectView submitBorrar(@PathVariable Integer id) {
		comercialService.deleteComercial(id);
		return new RedirectView("/comerciales");
	}

}

