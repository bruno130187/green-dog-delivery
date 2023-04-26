package com.bruno.casadocodigo.greendogdelivery.api;

public enum FluxoPedidoEnum {

	CHEGOU_NA_COZINHA("Chegou na cozinha"),
	SAIU_PARA_ENTREGA("Saiu para entrega"),
	FINALIZADO("Finalizado"),
	CANCELADO("Cancelado"),
	NAO_ENCONTRADO("Não encontrado");

	private final String displayValue;

	private FluxoPedidoEnum(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}
	
}
