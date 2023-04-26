package com.bruno.casadocodigo.greendogdelivery.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bruno.casadocodigo.greendogdelivery.api.FluxoPedidoEnum;
import com.bruno.casadocodigo.greendogdelivery.entity.Cep;
import com.bruno.casadocodigo.greendogdelivery.service.ValidaCepEntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import com.bruno.casadocodigo.greendogdelivery.entity.Item;
import com.bruno.casadocodigo.greendogdelivery.entity.Pedido;
import com.bruno.casadocodigo.greendogdelivery.dto.RespostaDTO;
import com.bruno.casadocodigo.greendogdelivery.repository.ClienteRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.ItemRepository;
import com.bruno.casadocodigo.greendogdelivery.util.EnviaNotificacao;

@RestController 
public class NovoPedidoController {

	private final ClienteRepository clienteRepository;
	private final ItemRepository itemRepository;
	private final EnviaNotificacao enviaNotificacao;
	private final ValidaCepEntregaService validaCepEntregaService;

	@Autowired
	public NovoPedidoController(ClienteRepository clienteRepository, ItemRepository itemRepository, EnviaNotificacao enviaNotificacao, ValidaCepEntregaService validaCepEntregaService) {
		this.clienteRepository =clienteRepository;
		this.itemRepository=itemRepository;
		this.enviaNotificacao = enviaNotificacao;
		this.validaCepEntregaService = validaCepEntregaService;
	}

	@GetMapping("/rest/pedido/novo/{clienteId}/{listaDeItens}")
	public RespostaDTO novo(@PathVariable("clienteId") Long clienteId, @PathVariable("listaDeItens") String listaDeItens) {

		RespostaDTO dto = new RespostaDTO();

		try {
			
			Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
			Cliente cliente = clienteOpt.get();

			String[] listaDeItensID = listaDeItens.split(",");

			Pedido pedido = new Pedido();

			BigDecimal valorTotal = BigDecimal.ZERO;
			List<Item> itensPedidos = new ArrayList<Item>();

			for (String itemId : listaDeItensID) {
				
				Optional<Item> itemOpt = itemRepository.findById(Long.parseLong(itemId));
				Item item = itemOpt.get();
				 
				itensPedidos.add(item);
				valorTotal = valorTotal.add(item.getPreco());
			}
			pedido.setItens(itensPedidos);
			pedido.setValorTotal(valorTotal);
			pedido.setData(new Date());
			pedido.setCliente(cliente);
			pedido.setStatus(FluxoPedidoEnum.CHEGOU_NA_COZINHA.name());
			cliente.getPedidos().add(pedido);

			this.clienteRepository.saveAndFlush(cliente);
			
			enviaNotificacao.enviaEmail(cliente,pedido);
			
			List<Long> pedidosID = new ArrayList<Long>();
			for (Pedido p : cliente.getPedidos()) {
				pedidosID.add(p.getId());
			}

			Long ultimoPedido = Collections.max(pedidosID);

			dto = new RespostaDTO(ultimoPedido, valorTotal,"Pedido efetuado com sucesso!");

		} catch (Exception e) {
			dto.setMensagem("Erro: " + e.getMessage());
		}
		return dto;

	}

	@GetMapping("/rest/pedido/novo/cep/{cep}")
	public Cep validaCep(@PathVariable("cep") String cep) {
		Cep cepRetorno = new Cep();
		cepRetorno = validaCepEntregaService.processa(cep);
		return cepRetorno;
	}

}
