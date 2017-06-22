package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.FormaPagamento;
import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.ClienteRepository;
import com.algaworks.pedidovenda.repository.ProdutoRepository;
import com.algaworks.pedidovenda.repository.UsuarioRepository;
import com.algaworks.pedidovenda.service.CadastroPedidoService;
import com.algaworks.pedidovenda.util.jsf.JsfUtil;

// @ManagedBean(name="cadastroPedidoBean")
@Named("cadastroPedidoBean")
@ViewScoped
public class CadastroPedidoBean implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;
	@Inject
	private ClienteRepository clienteRepository;
	@Inject
	private ProdutoRepository ProdutoRepository;
	@Inject
	private CadastroPedidoService cadastroPedidoService;
	//@Inject
	@Produces @PedidoEdicao
	private Pedido pedido;
	private String sku;
	private Produto produtoLinhaEditavel;
	private List<Usuario> vendedores;

	public CadastroPedidoBean() {
		criarNovoPedido();
	}

	private void criarNovoPedido() {
		this.pedido = new Pedido();
	}

	public void inicializar() {

		if (JsfUtil.isNotPostBack()) {

			if (this.pedido == null) {
				this.criarNovoPedido();
			}

			this.vendedores = this.usuarioRepository.buscarVendedores();

			this.pedido.adicionarItemVazio();

			this.recalcularPedido();
		}
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clienteRepository.porNome(nome);
	}

	public String salvar() {
		this.pedido.removerItemVazio();
		
		try{
			this.pedido = this.cadastroPedidoService.guardar(this.pedido);
			JsfUtil.addInfoMessage("Pedido Salvo com sucesso!");
			}finally{
				this.pedido.adicionarItemVazio();
			}
		
		return "";
	}

	public boolean isEditando() {
		return this.pedido.getId() != null;
	}

	public String recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}

		return "";
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.ProdutoRepository.porSku(this.sku);
			this.adicionarItem();
		}
	}

	public void adicionarItem() {
		ItemPedido item = this.pedido.getItens().get(0);

		if (this.produtoLinhaEditavel != null) {
			if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
				JsfUtil.addErrorMessage("Já existe um item no pedido com o produto informado!");
			} else {

				item.setProduto(this.produtoLinhaEditavel);
				item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());

				this.pedido.adicionarItemVazio();
				this.produtoLinhaEditavel = null;
				this.sku = null;
				this.pedido.recalcularValorTotal();
			}
		}
	}

	private boolean existeItemComProduto(Produto produto) {
		boolean exiteItem = false;

		for (ItemPedido item : this.pedido.getItens()) {
			if (produto.equals(item.getProduto())) {
				exiteItem = true;
				break;
			}
		}
		return exiteItem;
	}

	public List<Produto> completarProduto(String nome) {
		return this.ProdutoRepository.porNome(nome);
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().removerItem(linha);
			}
		}
		this.pedido.recalcularValorTotal();
	}
	
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event){
		this.pedido = event.getPedido();
	}
}
