package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.service.EmissaoPedidoService;
import com.algaworks.pedidovenda.util.jsf.JsfUtil;

@Named("emissaoPedidoBean")
@RequestScoped
public class EmissaoPedidoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject @PedidoEdicao
	private Pedido pedido;
	@Inject
	private EmissaoPedidoService emissaoPedidoService;
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	
	public String emitirPedido(){
		this.pedido.removerItemVazio();
		
		try{
			this.pedido = this.emissaoPedidoService.emitir(this.pedido);
			
			PedidoAlteradoEvent pedidoAlterado = new PedidoAlteradoEvent(this.pedido);
			this.pedidoAlteradoEvent.fire(pedidoAlterado);
			
			JsfUtil.addInfoMessage("Pedido emitido com sucesso!");
		}finally{
			this.pedido.adicionarItemVazio();
		}
		
		return "";
	}

}
