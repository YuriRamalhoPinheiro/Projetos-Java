package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.ProdutoRepository;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.util.jsf.JsfUtil;

@Named("pesquisaDeProdutosBean")
@ViewScoped
public class PesquisaProdutosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoRepository produtoRepository;
	private ProdutoFilter produtoFilter;
	private List<Produto> produtosFiltrados;
		
	private Produto produtoSelecionado;
	
	public PesquisaProdutosBean() {
		this.produtoFilter = new ProdutoFilter();
	}

	public String filtrarProdutos() {
		this.produtosFiltrados = this.produtoRepository.filtrados(this.produtoFilter);
		return "";
	}
	
	public String excluir(){
		this.produtoRepository.excluir(this.produtoSelecionado);	
		this.produtosFiltrados.remove(this.produtoSelecionado);
		JsfUtil.addInfoMessage("Produto " + this.produtoSelecionado.getSku() + " excluído com sucesso!");
		return "";
	}
	
	public List<Produto> getProdutosFiltrados() {
		return produtosFiltrados;
	}

	public ProdutoFilter getProdutoFilter() {
		return produtoFilter;
	}

	public void setProdutoFilter(ProdutoFilter produtoFilter) {
		this.produtoFilter = produtoFilter;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}
	
}
