package com.bruno.casadocodigo.greendogdelivery.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MensagemDTO {

	private String mensagem;

	private String servidor;

	private String debug;

	public MensagemDTO(String mensagem,String servidor,String debug) {
		this.mensagem = mensagem;
		this.servidor = servidor;
		this.debug = debug;
	}

	@Override
	public String toString() {
		return "MensagemDTO [mensagem=" + mensagem + ", servidor=" + servidor + ", debug=" + debug + "]";
	}

}
