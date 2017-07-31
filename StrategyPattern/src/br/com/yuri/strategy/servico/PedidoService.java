package br.com.yuri.strategy.servico;

import br.com.yuri.strategy.modelo.Pedido;
import br.com.yuri.strategy.modelo.TipoPedido;

public class PedidoService {
	
	public static void main(String[] args) {
			PedidoService.gerarPedido();
	}
	
	private static void gerarPedido() {
		Pedido pedido = new Pedido(TipoPedido.ATACADO);
		Double valorFinal = pedido.calcularValorTotal();
		
		System.out.println(valorFinal);
	} 
}
