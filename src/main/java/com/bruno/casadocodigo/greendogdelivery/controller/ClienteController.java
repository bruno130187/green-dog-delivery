
package com.bruno.casadocodigo.greendogdelivery.controller;

import javax.validation.Valid;

import com.bruno.casadocodigo.greendogdelivery.api.FluxoPedidoEnum;
import com.bruno.casadocodigo.greendogdelivery.entity.Log;
import com.bruno.casadocodigo.greendogdelivery.entity.Pedido;
import com.bruno.casadocodigo.greendogdelivery.repository.LogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import com.bruno.casadocodigo.greendogdelivery.repository.ClienteRepository;

import java.util.Date;
import java.util.Optional;

import static com.bruno.casadocodigo.greendogdelivery.entity.Cliente.SORT_BY_CLIENTE_NOME_AT_ASC;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	private final ClienteRepository clienteRepository;
	private final LogRepository logRepository;
	private final String CLIENTE_URI = "clientes/";
	private final String MODULO = "Cliente";

	public ClienteController(ClienteRepository clienteRepository, LogRepository logRepository) {
		this.clienteRepository = clienteRepository;
		this.logRepository = logRepository;
	}

	@GetMapping("/")
	public ModelAndView list() {
		Iterable<Cliente> clientes = this.clienteRepository.findAll(SORT_BY_CLIENTE_NOME_AT_ASC);
		return new ModelAndView(CLIENTE_URI + "list", "clientes", clientes);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Cliente cliente) {
		return new ModelAndView(CLIENTE_URI + "view", "cliente",cliente);
	}

	@GetMapping("/novo")
	public String createForm(@ModelAttribute Cliente cliente) {
		return CLIENTE_URI + "form";
	}

	@PostMapping(params = "form")
	public ModelAndView create(@Valid Cliente cliente,BindingResult result,RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView(CLIENTE_URI + "form", "formErrors", result.getAllErrors());
		}

		String message = "";

		if (cliente.getId() != null) {
			message = "Cliente alterado com sucesso!";
		} else {
			cliente.setStatus(true);
			message = "Cliente inserido com sucesso!";
		}

		cliente = this.clienteRepository.save(cliente);

		salvarLog(message, "SISTEMA");

		redirect.addFlashAttribute("globalMessage", message);
		return new ModelAndView("redirect:/" + CLIENTE_URI + "{cliente.id}", "cliente.id", cliente.getId());
	}

	@GetMapping(value = "cancelar/{id}")
	public ModelAndView cancelar(@PathVariable("id") Long id,RedirectAttributes redirect) {
		//this.clienteRepository.deleteById(id);
		Optional<Cliente> clienteOpt = clienteRepository.findById(id);
		Cliente cliente = clienteOpt.get();
		cliente.setStatus(false);
		clienteRepository.save(cliente);
		clienteRepository.flush();
		Iterable<Cliente> clientes = this.clienteRepository.findAll(SORT_BY_CLIENTE_NOME_AT_ASC);
		String message = "Cliente cancelado com sucesso!";
		ModelAndView mv = new ModelAndView(CLIENTE_URI + "list","clientes",clientes);
		mv.addObject("globalMessage", message);
		salvarLog(message, "SISTEMA");
		return mv;
	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Cliente cliente) {
		return new ModelAndView(CLIENTE_URI + "form","cliente",cliente);
	}

	public void salvarLog(String mensagem, String usuario){
		Log log = new Log();
		log.setModulo(MODULO);
		log.setData(new Date());
		log.setAcao(mensagem);
		log.setUsuario(usuario);
		this.logRepository.save(log);
	}

}
