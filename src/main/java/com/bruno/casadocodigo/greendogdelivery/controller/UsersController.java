
package com.bruno.casadocodigo.greendogdelivery.controller;

import com.bruno.casadocodigo.greendogdelivery.entity.Log;
import com.bruno.casadocodigo.greendogdelivery.entity.User;
import com.bruno.casadocodigo.greendogdelivery.repository.LogRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.PermissionRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bruno.casadocodigo.greendogdelivery.entity.Item.SORT_BY_ITEM_NOME_AT_ASC;
import static com.bruno.casadocodigo.greendogdelivery.entity.Permission.SORT_BY_PERMISSION_DESCRIPTION_AT_ASC;

@Controller
@RequestMapping("/users")
public class UsersController {

	private final UserRepository userRepository;
	private final LogRepository logRepository;
	private final PermissionRepository permissionRepository;
	private final String ITEM_URI = "users/";
	private final String MODULO = "Users";

	@Autowired
	private PasswordEncoder passwordEncoder;

	public UsersController(UserRepository userRepository, LogRepository logRepository, PermissionRepository permissionRepository) {
		this.userRepository = userRepository;
		this.logRepository = logRepository;
		this.permissionRepository = permissionRepository;
	}

	@GetMapping("/")
	public ModelAndView list() {
		Iterable<User> users = this.userRepository.findAll();
		return new ModelAndView(ITEM_URI + "list","users",users);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Long id) {
		try {
			ModelAndView mav = new ModelAndView(ITEM_URI + "view");
			User user = userRepository.findById(id).get();
			mav.addObject("user", user);
			return mav;
		} catch (Exception ex) {
			Iterable<User> users = this.userRepository.findAll();
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","users", users);
			String errorMessage = "Nenhum usuário encontrado com o ID " + id + " !";
			mv.addObject("errorMessage", errorMessage);
			return mv;
		}
	}

	@GetMapping("/novo")
	public String createForm(@ModelAttribute User user) {
		return ITEM_URI + "form";
	}

	@PostMapping(params = "form")
	public ModelAndView create(@Valid User user, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) {
			return new ModelAndView(ITEM_URI + "form","formErrors",result.getAllErrors());
		}

		String message = "";

		if (user.getId() != null) {
			message = "Usuário alterado com sucesso!";
		} else {
			user.setEnabled(true);
			message = "Usuário inserido com sucesso!";
		}

		if (user.getPassword().isEmpty()) {
			user.setPassword(userRepository.getPassAtual(user.getId()));
		} else {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		user = this.userRepository.save(user);

		salvarLog(message, "SISTEMA");

		redirect.addFlashAttribute("globalMessage",message);
		return new ModelAndView("redirect:/" + ITEM_URI + "{user.id}","user.id", user.getId());
	}

	@GetMapping(value = "remover/{id}")
	public ModelAndView remover(@PathVariable("id") Long id) {

		try {
			this.userRepository.deleteById(id);
			Iterable<User> users = this.userRepository.findAll();
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","users", users);
			String message = "Usuário removido com sucesso!";
			mv.addObject("globalMessage", message);
			salvarLog(message, "SISTEMA");
			return mv;
		} catch (Exception ex) {
			Iterable<User> users = this.userRepository.findAll();
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","users", users);
			String errorMessage = ex.getMessage();
			mv.addObject("errorMessage", errorMessage);
			return mv;
		}

	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Long userId) {
		User user = userRepository.findById(userId).get();
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("todasPermissoes", permissionRepository.findAll(SORT_BY_PERMISSION_DESCRIPTION_AT_ASC));
		model.put("user", user);
		return new ModelAndView(ITEM_URI + "form", model);
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
