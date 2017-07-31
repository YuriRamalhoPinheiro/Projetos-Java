package br.com.yuri.strategy.modelo;

public class Pedido {
	
	private Double valorTotal;
	private TipoPedido tipoPedido;
	
	public Pedido(TipoPedido tipoPedido) {
		this.tipoPedido = tipoPedido;
		this.valorTotal = 150.50;
	}
	
	/**
	 *Calcula o desconto sobre o <code>valorTotal</code> do pedido, 
	 *conforme o tipo de pedido escolhido
	 *
	 *@see    <code>TipoPedido</code>  ENUM que define os tipos de pedidos possíveis, ex: VAREJO, ATACADO
	 *@return  <code>Double</code>      valor do desconto aplicado sobre o <code>valorTotal</code>  
	 */
	public Double calcularDesconto() {
		 CalculadoraDeDesconto desconto = this.tipoPedido.calculadora();
		 return desconto.calcular(valorTotal);
	}
	
	public Double calcularValorTotal() {
		//implementar regra para calcular o valor total
		Double desconto = calcularDesconto();
		
		return this.valorTotal -= desconto;
	}
	
}
