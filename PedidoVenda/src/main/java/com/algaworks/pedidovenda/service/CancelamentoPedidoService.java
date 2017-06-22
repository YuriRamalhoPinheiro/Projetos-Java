package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.repository.PedidoRepository;
import com.algaworks.pedidovenda.util.jpa.Transactional;

/** Classe de serviço para cancelamento de Pedidos*/
public class CancelamentoPedidoService implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PedidoRepository pedidoRepository;
	@Inject
	private EstoqueService estoqueService;
	
	/** Altera o estatus do pedido para Cacelado e retorna os itens
	 * do pedido para o estoque */
	@Transactional
	public Pedido cancelar(Pedido pedido) {
		pedido = this.pedidoRepository.porId(pedido.getId());
		
		if(pedido.isNaoCancelavel()){
			throw new NegocioException("Pedido não pode ser cancelado no status " +
						pedido.getStatus().getDescricao() + ".");
		}
		
		if(pedido.isEmitido()){
			this.estoqueService.retornarItensParaEstoque(pedido);
		}
		
		pedido.mudarStatusParaCancelado();
		pedido = this.pedidoRepository.guardar(pedido);
		
		return pedido;
	}

}
