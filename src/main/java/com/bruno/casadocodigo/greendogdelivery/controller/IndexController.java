
package com.bruno.casadocodigo.greendogdelivery.controller;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bruno.casadocodigo.greendogdelivery.dto.MensagemDTO;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private Environment environment;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/ambiente")
	public String ambiente() {
		return "ambiente";
	}

	@GetMapping("properties")
	@ResponseBody
	Properties properties() {
		return System.getProperties();
	}

	@GetMapping("/delivery")
	public String delivery() {
		return "delivery/index";
	}

	@GetMapping("/profile")
	@ResponseBody
	public String[] profile() {
		return this.environment.getActiveProfiles();
	}

	@GetMapping("/server")
	@ResponseBody
	public String server(HttpServletRequest request) {
		return request.getServerName() + ":" + request.getServerPort();
	}

    @Value("${mensagem}")
    private String message;

    @Value("${debug}")
    private String debug;

    @GetMapping("/oferta")
    @ResponseBody
    public MensagemDTO getMessage(HttpServletRequest request) {
		if (this.debug.equalsIgnoreCase("1")) {
			return new MensagemDTO(this.message, request.getServerName() + ":" + request.getServerPort(), this.debug);
		} else {
			return new MensagemDTO("Sem ofertas no momento!", request.getServerName() + ":" + request.getServerPort(), this.debug);
		}
    }

	@GetMapping("/login")
	public String login() {
		return "login";
	}
    
}
