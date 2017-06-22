package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.PedidoRepository;
import com.algaworks.pedidovenda.repository.filter.PedidoFilter;

@Named("pesquisaPedidoBean")
@RequestScoped
public class PesquisaPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	PedidoRepository pedidoRepository;

	private PedidoFilter filter;
	private List<Pedido> pedidosFiltrados;

	public PesquisaPedidoBean() {
		this.filter = new PedidoFilter();
		this.pedidosFiltrados = new ArrayList<Pedido>();
	}

	public String pesquisar() {
		this.pedidosFiltrados = this.pedidoRepository.filtrados(this.filter);
		return "";
	}

	public StatusPedido[] getStatuses() {
		return StatusPedido.values();
	}

	public List<Pedido> getPedidosFiltrados() {
		return this.pedidosFiltrados;
	}

	public PedidoFilter getFilter() {
		return this.filter;
	}

	public void setFilter(PedidoFilter filter) {
		this.filter = filter;
	}

}
