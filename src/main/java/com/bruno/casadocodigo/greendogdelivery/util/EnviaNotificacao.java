package com.bruno.casadocodigo.greendogdelivery.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bruno.casadocodigo.greendogdelivery.entity.Cliente;
import com.bruno.casadocodigo.greendogdelivery.entity.Pedido;
import com.bruno.casadocodigo.greendogdelivery.dto.Notificacao;

@Component
public class EnviaNotificacao {

    @Autowired
    Notificacao  notificacao;
    
	public void enviaEmail(Cliente cliente, Pedido pedido) {
		
		System.out.println("Enviar notificacao para "+cliente.getNome() + " - pedido $"+pedido.getValorTotal());
		
		if (notificacao.envioAtivo()) {
			/*
			     codigo de envio
			 */
			System.out.println("Notificacao enviada!");
		} else {
			System.out.println("Notificacao desligada!");
		}

	}
	
}
