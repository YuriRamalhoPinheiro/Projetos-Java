package com.algaworks.pedidovenda.service;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.PedidoRepository;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepository;

	@Transactional
	public Pedido guardar(Pedido pedido) throws NegocioException {

		if (pedido.isNovo()) {
			pedido.setDataCriacao(new Date());
			pedido.setStatus(StatusPedido.ORCAMENTO);
		}

		pedido.recalcularValorTotal();

		validarPedido(pedido);

		pedido = this.pedidoRepository.guardar(pedido);
		return pedido;
	}

	private void validarPedido(Pedido pedido) {
		if (pedido.isNaoAlteravel()) {
			throw new NegocioException(
					"O pedido não poder ser alterado no status " + pedido.getStatus().getDescricao() + ".");
		}

		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("O pedido deve possuir pelo menos um item!");
		}

		if (pedido.isValorTotalNegativo()) {
			throw new NegocioException("O valor Total do Pedido não pode ser negativo!");
		}
	}
}
