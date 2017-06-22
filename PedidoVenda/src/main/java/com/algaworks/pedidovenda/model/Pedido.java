package com.algaworks.pedidovenda.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date dataCriacao;
	private String observacao;
	private Date dataEntrega;
	private BigDecimal valorFrete = BigDecimal.ZERO;
	private BigDecimal valorDesconto = BigDecimal.ZERO;
	private BigDecimal valorTotal = BigDecimal.ZERO;
	private StatusPedido status = StatusPedido.ORCAMENTO;
	private FormaPagamento formaPagamento;
	private Usuario vendedor;
	private Cliente cliente;
	private EnderecoEntrega enderecoEntrega;
	private List<ItemPedido> itens = new ArrayList<ItemPedido>();

	public Pedido() {
		this.dataCriacao = new Date(); // TODO Alterar a localização da data
										// para Brasil
		this.enderecoEntrega = new EnderecoEntrega();
	}

	@Transient
	public boolean isNovo() {
		return this.id == null;
	}

	@Transient
	public boolean isExistente() {
		return !isNovo();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false)
	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	@Column(name = "observacao", columnDefinition = "text")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "data_entrega", nullable = false)
	public Date getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(Date dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	@NotNull
	@Column(name = "valor_frete", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	@NotNull
	@Column(name = "valor_desconto", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@NotNull
	@Column(name = "valor_total", nullable = false, precision = 10, scale = 2)
	public BigDecimal getValorTotal() {
		return this.valorTotal;
	}

	public void setValorTotal(BigDecimal valorToral) {
		this.valorTotal = valorToral;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	public StatusPedido getStatus() {
		return status;
	}

	public void setStatus(StatusPedido status) {
		this.status = status;
	}

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "forma_pagamento", nullable = false, length = 20)
	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "vendedor_id", nullable = false)
	public Usuario getVendedor() {
		return vendedor;
	}

	public void setVendedor(Usuario vendedor) {
		this.vendedor = vendedor;
	}

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cliente_id", nullable = false)
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Embedded
	public EnderecoEntrega getEnderecoEntrega() {
		return enderecoEntrega;
	}

	public void setEnderecoEntrega(EnderecoEntrega enderecoEntrega) {
		this.enderecoEntrega = enderecoEntrega;
	}

	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	public List<ItemPedido> getItens() {
		return this.itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Transient
	public BigDecimal getSubTotal() {
		return this.getValorTotal().subtract(this.getValorFrete()).add(this.getValorDesconto());
	}

	public void recalcularValorTotal() {
		BigDecimal valorTotal = BigDecimal.ZERO;
		valorTotal = valorTotal.add(this.getValorFrete()).subtract(this.getValorDesconto());

		for (ItemPedido item : this.getItens()) {
			if (item.getProduto() != null && item.getProduto().getId() != null) {
				valorTotal = valorTotal.add(item.getValorTotal());
			}
		}
		System.out.println(valorTotal);
		this.setValorTotal(valorTotal);

	}

	public void adicionarItemVazio() {
		if (this.isOrcamento()) {
			Produto produto = new Produto();

			ItemPedido itemPedido = new ItemPedido();
			itemPedido.setProduto(produto);
		//	itemPedido.setQuantidade(1);
			itemPedido.setPedido(this);

			this.getItens().add(0, itemPedido);
		}
	}
	
	@Transient
	public boolean isOrcamento() {
		return StatusPedido.ORCAMENTO.equals(this.getStatus());
	}

	public void removerItem(int linha) {
		this.getItens().remove(linha);
	}
	
	public void removerItemVazio(){
		ItemPedido itemPedido = this.getItens().get(0);
		
		if(itemPedido != null && itemPedido.getProduto().getId() == null){
			this.getItens().remove(0);
		}
	}

	@Transient
	public boolean isValorTotalNegativo() {
		return this.getValorTotal().compareTo(BigDecimal.ZERO) < 0;
	}
	
	@Transient
	public boolean isEmitido() {
		return StatusPedido.EMITIDO.equals(this.status);
	}
	
	@Transient
	public boolean isNaoEmissivel() {
		return  !isEmissivel();
	}
	
	@Transient
	private boolean isEmissivel() {
		return this.isExistente() && this.isOrcamento();
	}

	@Transient
	public boolean isNaoCancelavel() {
		return !isCancelavel();
	}
	
	@Transient
	private boolean isCancelavel() {
		return this.isExistente() && !this.isCancelado();
	}
	
	@Transient
	public boolean isCancelado() {
		return StatusPedido.CANCELADO.equals(this.getStatus());
	}	
	
	
	public void mudarStatusParaCancelado() {
		this.setStatus(StatusPedido.CANCELADO);
	}
	
	public void mudarStatusParaEmitido() {
		this.setStatus(StatusPedido.EMITIDO);
	}
	
	public void mudarStatusParaOrcamento() {
		this.setStatus(StatusPedido.ORCAMENTO);
	}
	
	@Transient
	public boolean isNaoAlteravel() {
		return !isAlteravel();
	}
	
	@Transient
	private boolean isAlteravel() {
		return this.isOrcamento();
	}
	
	@Transient
	public boolean isNaoEnviavelPorEmail(){
		return this.isNovo() || this.isCancelado();
	}
}
