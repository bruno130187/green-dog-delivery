
package com.bruno.casadocodigo.greendogdelivery.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.bruno.casadocodigo.greendogdelivery.api.FluxoPedidoEnum;
import com.bruno.casadocodigo.greendogdelivery.entity.*;
import com.bruno.casadocodigo.greendogdelivery.repository.LogRepository;
import com.bruno.casadocodigo.greendogdelivery.service.ValidaCepEntregaService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bruno.casadocodigo.greendogdelivery.repository.ClienteRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.ItemRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.PedidoRepository;

import static com.bruno.casadocodigo.greendogdelivery.entity.Cliente.SORT_BY_CLIENTE_NOME_AT_ASC;
import static com.bruno.casadocodigo.greendogdelivery.entity.Item.SORT_BY_ITEM_NOME_AT_ASC;
import static com.bruno.casadocodigo.greendogdelivery.entity.Pedido.SORT_BY_PEDIDO_ID_AT_DESC;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

	private final PedidoRepository pedidoRepository;
	private final ItemRepository itemRepository;
	private final ClienteRepository clienteRepository;
	private final LogRepository logRepository;
	private final ValidaCepEntregaService validaCepEntregaService;
	private final String ITEM_URI = "pedidos/";
	private final String MODULO = "Pedido";

	public PedidoController(PedidoRepository pedidoRepository, ItemRepository itemRepository, ClienteRepository clienteRepository, LogRepository logRepository, ValidaCepEntregaService validaCepEntregaService) {
		this.pedidoRepository = pedidoRepository;
		this.itemRepository = itemRepository;
		this.clienteRepository = clienteRepository;
		this.logRepository = logRepository;
		this.validaCepEntregaService = validaCepEntregaService;
	}

	//Não funciona por causa das Constraints no BD
	public void DeletePedidoItens(Long id) throws SQLException, ClassNotFoundException {
		Connection connection = null;
		Statement statement = null;

		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://localhost:1433;databaseName=greendog;encrypt=false;trustServerCertificate=true";
			String user = "sa";
			String pass = "A_Str0ng_Required_Password";
			connection = DriverManager.getConnection(url, user, pass);
			statement = connection.createStatement();
			String sql = " DELETE FROM pedido WHERE id = " + id;
			statement.executeUpdate(sql);
			System.out.println("Delete feito para o pedido_itens com pedido_id: " + id);
			statement.close();
			connection.close();
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	@GetMapping("/")
	public ModelAndView list() throws SQLException, ClassNotFoundException {
		Iterable<Pedido> pedidos = this.pedidoRepository.findAll(SORT_BY_PEDIDO_ID_AT_DESC);
		return new ModelAndView(ITEM_URI + "list", "pedidos", pedidos);
	}

	@GetMapping("{id}")
	public ModelAndView view(@PathVariable("id") Pedido pedido) {
		return new ModelAndView(ITEM_URI + "view", "pedido",pedido);
	}

	@GetMapping("/novo")
	public ModelAndView createForm(@ModelAttribute Pedido pedido) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("todosItens", itemRepository.findAllItensAtivos(SORT_BY_ITEM_NOME_AT_ASC));
		model.put("todosClientes", clienteRepository.findAllClientesAtivos(SORT_BY_CLIENTE_NOME_AT_ASC));
		return new ModelAndView(ITEM_URI + "form", model);
	}

	@PostMapping(params = "form")
	public ModelAndView create(@Valid Pedido pedido, BindingResult result, RedirectAttributes redirect) {
		if (result.hasErrors()) { return new ModelAndView(ITEM_URI + "form","formErrors",result.getAllErrors()); }

		String message = "";

		if (pedido.getId() != null) {

			Optional<Pedido> pedidoParaAlterarOpt = pedidoRepository.findById(pedido.getId());
			Pedido pedidoParaAlterar = pedidoParaAlterarOpt.get();

			Optional<Cliente> clienteOpt = clienteRepository.findById(pedidoParaAlterar.getCliente().getId());
			Cliente cliente = clienteOpt.get();

			pedidoParaAlterar.setItens(pedido.getItens());
			BigDecimal valorTotal = BigDecimal.ZERO;

			for (Item i : pedido.getItens()) {
				valorTotal = valorTotal.add(i.getPreco());
			}

			pedidoParaAlterar.setData(pedido.getData());
			pedidoParaAlterar.setValorTotal(valorTotal);
			cliente.getPedidos().remove(pedidoParaAlterar);
			cliente.getPedidos().add(pedidoParaAlterar);
			this.clienteRepository.save(cliente);

			message = "Pedido alterado com sucesso!";
			salvarLog(message, "SISTEMA");

		} else {

			Optional<Cliente> clienteOpt = clienteRepository.findById(pedido.getCliente().getId());
			Cliente cliente = clienteOpt.get();

			BigDecimal valorTotal = BigDecimal.ZERO;
			for (Item i : pedido.getItens()) {
				valorTotal = valorTotal.add(i.getPreco());
			}
			pedido.setValorTotal(valorTotal);
			pedido.setStatus(String.valueOf(FluxoPedidoEnum.CHEGOU_NA_COZINHA));
			pedido = this.pedidoRepository.save(pedido);
			cliente.getPedidos().add(pedido);
			this.clienteRepository.save(cliente);

			message = "Pedido inserido com sucesso!";
			salvarLog(message, "SISTEMA");

		}

		redirect.addFlashAttribute("globalMessage",  message);
		return new ModelAndView("redirect:/" + ITEM_URI + "{pedido.id}", "pedido.id", pedido.getId());

	}

	@GetMapping(value = "cancelar/{id}")
	public ModelAndView cancelar(@PathVariable("id") Long id) {

		try {
			//this.pedidoRepository.deleteById(id);
			Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
			Pedido pedido = pedidoOpt.get();
			pedido.setStatus(FluxoPedidoEnum.CANCELADO.name());
			pedidoRepository.save(pedido);
			pedidoRepository.flush();
			Iterable<Pedido> pedidos = this.pedidoRepository.findAll(SORT_BY_PEDIDO_ID_AT_DESC);
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","pedidos", pedidos);
			String message = "Pedido cancelado com sucesso!";
			mv.addObject("globalMessage", message);
			salvarLog(message, "SISTEMA");
			return mv;
		} catch (Exception ex) {
			Iterable<Pedido> pedidos = this.pedidoRepository.findAll(SORT_BY_PEDIDO_ID_AT_DESC);
			ModelAndView mv = new ModelAndView(ITEM_URI + "list","pedidos", pedidos);
			mv.addObject("errorMessage", ex.getMessage());
			return mv;
		}

	}

	@GetMapping(value = "alterar/{id}")
	public ModelAndView alterarForm(@PathVariable("id") Pedido pedido) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("todosItens",itemRepository.findAllItensAtivos(SORT_BY_ITEM_NOME_AT_ASC));
		model.put("todosClientes",clienteRepository.findAllClientesAtivos(SORT_BY_CLIENTE_NOME_AT_ASC));
		model.put("pedido",pedido);
		return new ModelAndView(ITEM_URI + "form", model);
	}

	@GetMapping("/validacep/{cep}")
	public @ResponseBody String validaCep(@PathVariable("cep") String cep) {
		String message = null;
		try {
			Cep cepRetorno = new Cep();
			cepRetorno = validaCepEntregaService.processa(cep);
			message = "";

			if (cepRetorno.getErro() != null && cepRetorno.getErro().equals("CEP inválido!")) {
				message = cepRetorno.getErro();
			} else if (cepRetorno.getErro() != null && cepRetorno.getErro().contains("Entrega indisponível para este CEP")) {
				message = cepRetorno.getErro();
			} else {
				message = "O seu pedido será entregue no CEP " + cepRetorno.getCep() + " em aproximadamente 55 minutos.";
			}

			return message;
		} catch (Exception ex) {
			message = ex.getMessage();
			return message;
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
