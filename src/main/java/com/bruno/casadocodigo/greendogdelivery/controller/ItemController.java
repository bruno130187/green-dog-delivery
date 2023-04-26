
package com.bruno.casadocodigo.greendogdelivery.controller;

import javax.validation.Valid;

import com.bruno.casadocodigo.greendogdelivery.entity.Log;
import com.bruno.casadocodigo.greendogdelivery.repository.LogRepository;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bruno.casadocodigo.greendogdelivery.entity.Item;
import com.bruno.casadocodigo.greendogdelivery.repository.ItemRepository;

import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/itens")
public class ItemController {

	private final ItemRepository itemRepository;
	private final LogRepository logRepository;
	private final String ITEM_URI = "itens/";
	private final String MODULO = "Item";

	public ItemController(ItemRepository itemRepository, LogRepository logRepository) {
		this.itemRepository = itemRepository;
		this.logRepository = logRepository;
	}

	@GetMapping("/")
	public ModelAndView list() {
		Iterable<Item> itens = this.itemRepository.findAll();
		return new ModelAndView(ITEM_URI + "list","itens",itens);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Long id) {
		try {
			ModelAndView mav = new ModelAndView(ITEM_URI + "view");
			Item item = itemRepository.findById(id).get();
			item.setPreco(item.getPreco().setScale(2, RoundingMode.HALF_UP));
			mav.addObject("item", item);
			return mav;
		} catch (Exception ex) {
			Iterable<Item> itens = this.itemRepository.findAll();
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","itens",itens);
			String errorMessage = "Nenhum item encontrado com o ID " + id + " !";
			mv.addObject("errorMessage", errorMessage);
			return mv;
		}
	}

	@GetMapping("/novo")
	public String createForm(@ModelAttribute Item item) {
		return ITEM_URI + "form";
	}

	@PostMapping(params = "form")
	public ModelAndView create(@Valid Item item, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView(ITEM_URI + "form","formErrors",result.getAllErrors());
		}

		String message = "";

		if (item.getId() != null) {
			message = "Item alterado com sucesso!";
		} else {
			item.setStatus(true);
			message = "Item inserido com sucesso!";
		}

		item = this.itemRepository.save(item);

		salvarLog(message, "SISTEMA");

		redirect.addFlashAttribute("globalMessage",message);
		return new ModelAndView("redirect:/" + ITEM_URI + "{item.id}","item.id",item.getId());
	}

	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {

		try {
			this.itemRepository.deleteById(id);
			Iterable<Item> itens = this.itemRepository.findAll();
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","itens",itens);
			String message = "Item removido com sucesso!";
			mv.addObject("globalMessage", message);
			salvarLog(message, "SISTEMA");
			return mv;
		} catch (Exception ex) {
			Iterable<Item> itens = this.itemRepository.findAll();
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","itens",itens);
			String errorMessage = ex.getMessage();
			mv.addObject("errorMessage", errorMessage);
			return mv;
		}

	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Long itemId) {
		ModelAndView mav = new ModelAndView(ITEM_URI + "form");
		Item item = itemRepository.findById(itemId).get();
		item.setPreco(item.getPreco().setScale(2, RoundingMode.HALF_UP));
		mav.addObject("item", item);
		return mav;
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
