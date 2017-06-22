package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.velocity.tools.generic.NumberTool;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.util.jsf.JsfUtil;
import com.algaworks.pedidovenda.util.mail.Mailer;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named("envioEmailPedidoBean")
@RequestScoped
public class EnvioEmailPedidoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	@Inject
	private Mailer mailer;
	
	public String enviarEmail(){
		
		VelocityTemplate templateDoEmail = new VelocityTemplate(getClass().getResourceAsStream("/emails/pedido.template"));
		
		MailMessage mensagem = mailer.novaMensagem();
		mensagem.to(this.pedido.getCliente().getEmail())
			.subject("Pedido " + this.pedido.getId())
			.bodyHtml(templateDoEmail)
			.put("pedido", this.pedido)
			.put("numberTool", new NumberTool())
			.put("locale", new Locale("pt", "BR"))
			.send();
			
		JsfUtil.addInfoMessage("Pedido enviado por E-mail com sucesso!");
		
		return "";
	}
}
