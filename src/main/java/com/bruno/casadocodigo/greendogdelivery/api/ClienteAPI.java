
package com.bruno.casadocodigo.greendogdelivery.api;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import com.bruno.casadocodigo.greendogdelivery.entity.Log;
import com.bruno.casadocodigo.greendogdelivery.repository.ClienteRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.LogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteAPI {

	private final ClienteRepository clienteRepository;
	private final LogRepository logRepository;
	private final String MODULO = "Cliente";

	public ClienteAPI(ClienteRepository clienteRepository, LogRepository logRepository) {
		this.clienteRepository = clienteRepository;
		this.logRepository = logRepository;
	}

	@GetMapping(value = "changeStatus/{id}/{status}")
	public String changeStatus(@PathVariable("id") Long id, @PathVariable boolean status) {
		try {
			Optional<Cliente> clienteOpt = clienteRepository.findById(id);
			Cliente cliente = clienteOpt.get();
			cliente.setStatus(status);
			clienteRepository.save(cliente);
			clienteRepository.flush();
			return "ok";
		} catch (Exception ex) {
			return ex.getMessage();
		}
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
