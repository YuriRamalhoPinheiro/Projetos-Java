package com.algaworks.pedidovenda.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Named("seguranca")
@RequestScoped
public class Seguranca {
	
	@Inject
	private ExternalContext externalContext; 
	
	public String getNomeUsuario() {

		UsuarioSistema usuarioLogado = getUsuarioLogado();
		if(usuarioLogado != null){
			return usuarioLogado.getNomeDoUsuario();
		}
		return "usuário";
	}
	
	@Produces @UsuarioLogado
	private UsuarioSistema getUsuarioLogado() {
		
		UsernamePasswordAuthenticationToken usuarioPrincipal = null;
		usuarioPrincipal = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance()
													.getExternalContext().getUserPrincipal();

		if (usuarioPrincipal != null && usuarioPrincipal.getPrincipal() != null) {
			return (UsuarioSistema) usuarioPrincipal.getPrincipal();
		}

		return null;
	}
	
	public boolean isEmitirPedidoPermitido(){
		return this.externalContext.isUserInRole("ADMINISTRADORES") || 
				this.externalContext.isUserInRole("VENDEDORES");
	}
	
	public boolean isCancelarPedidoPermitido(){
		return this.externalContext.isUserInRole("ADMINISTRADORES") || 
				this.externalContext.isUserInRole("VENDEDORES");
	}

	
}