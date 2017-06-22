package com.algaworks.pedidovenda.model;

public enum StatusPedido {
	
	ORCAMENTO("Or�amento"),
	EMITIDO("Emitido"),
	CANCELADO("Cancelado");
	
	private String descricao;
	
	private StatusPedido(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
}
