package com.bruno.casadocodigo.greendogdelivery.api;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import com.bruno.casadocodigo.greendogdelivery.entity.Item;
import com.bruno.casadocodigo.greendogdelivery.entity.Pedido;
import com.bruno.casadocodigo.greendogdelivery.repository.ClienteRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.ItemRepository;
import com.bruno.casadocodigo.greendogdelivery.repository.PedidoRepository;

import static com.bruno.casadocodigo.greendogdelivery.entity.Item.SORT_BY_ITEM_NOME_AT_ASC;

@RestController
@RequestMapping("/api")
public class PedidoAPI {

	private final PedidoRepository pedidoRepository;
	private final ClienteRepository clienteRepository;
	private final ItemRepository itemRepository;

	public PedidoAPI(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ItemRepository itemRepository) {
		this.pedidoRepository = pedidoRepository;
		this.clienteRepository = clienteRepository;
		this.itemRepository = itemRepository;
	}

	@PostMapping("/pedido")
	public Pedido fazPedido(@RequestBody NovoPedidoTO novoPedidoTO) {

		Pedido pedido = new Pedido();
		Optional<Cliente> clienteOpt = clienteRepository.findById(novoPedidoTO.getIdCliente());
		pedido.setCliente(clienteOpt.get());
		pedido.setData(new Date());
		pedido.setValorTotal(novoPedidoTO.getValorTotal());

		List<Item> itens = new ArrayList<>();
		for (Long idItem : novoPedidoTO.getItensId()) {
			Optional<Item> itemOpt = itemRepository.findById(idItem);
			Item item = itemOpt.get();
			itens.add(item);
		}
		pedido.setItens(itens);
		pedido.setStatus(FluxoPedidoEnum.CHEGOU_NA_COZINHA.name());

		pedidoRepository.save(pedido);

		return pedido;
	}

	@GetMapping("/pedido/{id}")
	public Pedido buscaPedido(@PathVariable Long id) {

		Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
		if (pedidoOpt.isPresent()) {
			Pedido pedidoBanco = new Pedido();
			pedidoBanco = pedidoOpt.get();
			for (FluxoPedidoEnum itemEnum : FluxoPedidoEnum.values()) {
				if (itemEnum.name().equalsIgnoreCase(pedidoBanco.getStatus())) {
					pedidoBanco.setStatus(itemEnum.getDisplayValue());
				}
			}
			return pedidoBanco;
		} else {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, FluxoPedidoEnum.NAO_ENCONTRADO.name());
		}
	}

	@DeleteMapping("/pedido/{id}")
	public void cancelaPedido(@PathVariable Long id) {

		Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
		Pedido pedido = pedidoOpt.get();
		if (!pedidoOpt.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, FluxoPedidoEnum.NAO_ENCONTRADO.name());
		}

		pedido.setStatus(FluxoPedidoEnum.CANCELADO.name());
		pedidoRepository.save(pedido);
		pedidoRepository.flush();

	}

	@GetMapping("/pedido/all")
	public List<Pedido> buscaTudo() {

		List<Pedido> pedidoLista = pedidoRepository.findAll();

		for (FluxoPedidoEnum itemEnum : FluxoPedidoEnum.values()) {
			for (Pedido pedido : pedidoLista) {
				if (itemEnum.toString().equalsIgnoreCase(pedido.getStatus())) {
					pedido.setStatus(itemEnum.getDisplayValue());
				}
			}
		}

		if (pedidoLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}

		return pedidoLista;
	}

	@GetMapping("/itens/all")
	public List<Item> buscaItensAtivos() {

		List<Item> itensLista = itemRepository.findAllItensAtivos(SORT_BY_ITEM_NOME_AT_ASC);

		if (itensLista.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT);
		}

		return itensLista;
	}

}
