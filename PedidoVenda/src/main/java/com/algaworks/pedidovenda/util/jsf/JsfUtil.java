package com.algaworks.pedidovenda.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class JsfUtil {

	private static FacesMessage facesMessage;

	public static boolean isPostBack() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	public static boolean isNotPostBack() {
		return !isPostBack();
	}

	public static void addErrorMessage(String mensagem) {
		exibirMensagem(mensagem, FacesMessage.SEVERITY_ERROR);	
	}

	public static void addInfoMessage(String mensagem) {
		exibirMensagem(mensagem, FacesMessage.SEVERITY_INFO);
	}

	private static void exibirMensagem(String mensagem, Severity sevirity) {
		facesMessage = new FacesMessage(sevirity, mensagem, mensagem);

		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
}
