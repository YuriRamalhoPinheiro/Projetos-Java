package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.CategoriaRepository;
import com.algaworks.pedidovenda.service.CadastroProdutoService;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jsf.JsfUtil;

@Named("cadastroProdutoBean")
@ViewScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaRepository categoriaRepository;
	@Inject
	private CadastroProdutoService cadastroProdutoService;

	private Produto produto;
	private Categoria categoriaPai;

	private List<Categoria> categoriasRaizes;
	private List<Categoria> subCategorias;

	public CadastroProdutoBean() {
		this.criarNovoProduto();
	}

	public void inicializar() {
		System.out.println("Inicializando página ...");

		if (this.produto == null) {
			this.criarNovoProduto();
		}

		this.categoriasRaizes = this.categoriaRepository.buscarCateogoriasRaizes();

		if (this.categoriaPai != null) {
			carregarSubCategorias();
		}
	}

	public void carregarSubCategorias() {
		this.subCategorias = this.categoriaRepository.buscarSubCategoriasDe(this.categoriaPai);
	}

	public void salvar() {
		try {
			this.produto = this.cadastroProdutoService.salvar(this.produto);
			criarNovoProduto();

			JsfUtil.addInfoMessage("Produto salvo com sucesso!");
		} catch (NegocioException exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	private void criarNovoProduto() {
		this.produto = new Produto();
		this.categoriaPai = null;
		this.subCategorias = new ArrayList<Categoria>();
	}

	public boolean isEditando() {
		return this.produto.getId() != null;
	}

	public Produto getProduto() {
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;

		if (this.produto != null) {
			this.categoriaPai = this.produto.getCategoria().getCategoriaPai();
		}
	}

	public List<Categoria> getCategoriasRaizes() {
		return this.categoriasRaizes;
	}

	@NotNull
	public Categoria getCategoriaPai() {
		return this.categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	public List<Categoria> getSubCategorias() {
		return this.subCategorias;
	}

}
