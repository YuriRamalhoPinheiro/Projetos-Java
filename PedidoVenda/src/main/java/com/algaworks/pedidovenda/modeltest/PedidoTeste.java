package com.algaworks.pedidovenda.modeltest;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.EnderecoEntrega;
import com.algaworks.pedidovenda.model.FormaPagamento;
import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.model.Usuario;

public class PedidoTeste {

	public static void main(String[] args) {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("PUPedidoVenda");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();

		Cliente cliente = manager.find(Cliente.class, 1L);
		Produto produto = manager.find(Produto.class, 1L);
		Usuario vendedor = manager.find(Usuario.class, 1L);

		EnderecoEntrega enderecoEntrega = new EnderecoEntrega();
		enderecoEntrega.setLogradouro("Rua Santa Rita");
		enderecoEntrega.setNumero("103");
		enderecoEntrega.setComplemento("Casa");
		enderecoEntrega.setCidade("Araçuaí");
		enderecoEntrega.setCep("39600-000");
		enderecoEntrega.setUf("Minas Gerais");

		Pedido pedido = new Pedido();
		pedido.setDataCriacao(new Date());
		pedido.setDataEntrega(new Date());
		pedido.setCliente(cliente);
		pedido.setVendedor(vendedor);
		pedido.setEnderecoEntrega(enderecoEntrega);
		pedido.setFormaPagamento(FormaPagamento.CARTAO_CREDITO);
		pedido.setObservacao("Aberto das 8hs as 18hs");
		pedido.setValorDesconto(BigDecimal.ZERO);
		pedido.setValorFrete(BigDecimal.ZERO);
		pedido.setValorTotal(new BigDecimal(22.30));
		pedido.setStatus(StatusPedido.EMITIDO);

		ItemPedido itemPedido = new ItemPedido();
		itemPedido.setPedido(pedido);
		itemPedido.setProduto(produto);
		itemPedido.setQuantidade(10);
		itemPedido.setValorUnitario(new BigDecimal(2.32));

		pedido.getItens().add(itemPedido);

		manager.persist(pedido);

		transaction.commit();

		manager.close();

		System.out.println(cliente.getNome());
		System.out.println(vendedor.getNome());
		System.out.println(produto.getNome());
	}
}
