package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.service.CancelamentoPedidoService;
import com.algaworks.pedidovenda.util.jsf.JsfUtil;

@Named("cancelarPedidoBean")
@RequestScoped
public class CancelarPedidoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private CancelamentoPedidoService cancelamentoPedidoService;
	@Inject
	private Event<PedidoAlteradoEvent> pedidoAlteradoEvent;
	@Inject @PedidoEdicao
	private Pedido pedido;
	
	public String cancelarPedido(){
		this.pedido = this.cancelamentoPedidoService.cancelar(this.pedido);
		
		this.pedidoAlteradoEvent.fire(new PedidoAlteradoEvent(this.pedido));
		
		JsfUtil.addInfoMessage("Pedido cancelado com sucesso!");
		
		return "";
	}
} 
