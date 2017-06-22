package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.ProdutoRepository;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroProdutoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public Produto salvar(Produto produto) throws NegocioException {

		Produto produtoExistente = produtoRepository.porSku(produto.getSku());

		if ((produtoExistente != null) && (!produtoExistente.equals(produto))) {
			throw new NegocioException("Já existe um produto com o SKU informado.");
		}

		return produtoRepository.guardar(produto);
	}
	
}
