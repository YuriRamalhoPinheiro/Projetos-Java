package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.repository.PedidoRepository;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class EstoqueService implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepository;
	
	@Transactional
	public void baixarItensEstoque(Pedido pedido) {
		pedido = this.pedidoRepository.porId(pedido.getId());
		
		for(ItemPedido item : pedido.getItens()){
			item.getProduto().baixarEstoque(item.getQuantidade());
		}
	}

	public void retornarItensParaEstoque(Pedido pedido) {
		pedido = pedidoRepository.porId(pedido.getId());
		
		for(ItemPedido item : pedido.getItens()){
			item.getProduto().adicionarNoEstoque(item.getQuantidade());
		}
		
	}

}
