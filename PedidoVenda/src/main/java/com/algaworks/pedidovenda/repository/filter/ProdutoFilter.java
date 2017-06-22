package com.algaworks.pedidovenda.repository.filter;

import java.io.Serializable;

public class ProdutoFilter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String Sku;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSku() {
		return Sku;
	}

	public void setSku(String sku) {
		Sku = (sku == null? null : sku.toUpperCase());
	}

}
