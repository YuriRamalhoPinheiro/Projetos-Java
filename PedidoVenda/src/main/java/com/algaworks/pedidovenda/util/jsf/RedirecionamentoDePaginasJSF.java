package com.algaworks.pedidovenda.util.jsf;

import java.io.IOException;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

public class RedirecionamentoDePaginasJSF implements RedirecionamentoDePaginas {

	/** Acessa o contexto da aplicação e redireciona o usuário */
	public void redirecionarPara(String pagina) {

		FacesContext faceContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = faceContext.getExternalContext();
		String contextPath = externalContext.getRequestContextPath();

		try {
			externalContext.redirect(contextPath + pagina);
			faceContext.responseComplete();
		} catch (IOException e) {
			new FacesException(e);
		}

	}

}
