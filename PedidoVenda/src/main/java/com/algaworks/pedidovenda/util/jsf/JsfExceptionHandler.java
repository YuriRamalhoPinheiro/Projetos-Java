package com.algaworks.pedidovenda.util.jsf;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.algaworks.pedidovenda.service.NegocioException;

public class JsfExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;
	private NegocioException negocioException;
	private RedirecionamentoDePaginas redirecionandoPagina;
	
	private static Log log  = LogFactory.getLog(JsfExceptionHandler.class);
	
	public JsfExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return this.wrapped;
	}

	/**
	 * Trata as excetions do ViewExpiredExeption causadas quando o ciclo de vida
	 * do bean expirar
	 */
	@Override
	public void handle() throws FacesException {
		/* Recupera em ordem de fila, todos as Exceptions não tratadas */
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

		/*
		 * Percorre todos as Exceptions da fila e verifica se são instâncias de
		 * ViewedExpiredException, caso verdadeiro, redireciona o usuário para a
		 * página inicial do sistema
		 */
		
		redirecionandoPagina = new RedirecionamentoDePaginasJSF();

		while (events.hasNext()) {
			ExceptionQueuedEvent event = events.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
			Throwable exception = context.getException();
			
			negocioException = new NegocioException().getNegocioException(exception);

			boolean excessaoTratada = false;

			try {
				
				if (exception instanceof ViewExpiredException) {
					excessaoTratada = true;
					redirecionandoPagina.redirecionarPara("/");
				} else if (negocioException != null) {
					excessaoTratada = true;
					JsfUtil.addErrorMessage(negocioException.getMessage());
				} else {
					excessaoTratada = true;
					log.error("Erro de Sistema: " + exception.getMessage(), exception);
					redirecionandoPagina.redirecionarPara("/erro.xhtml");
				} 
			} finally {
				if (excessaoTratada)
					events.remove();
			}
		}

		getWrapped().handle();
	}

}
