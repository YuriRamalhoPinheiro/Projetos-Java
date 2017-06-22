package com.algaworks.pedidovenda.service;

public class NegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NegocioException(String mensagem) {
		super(mensagem);
	}
	
	public NegocioException(){}

	public NegocioException getNegocioException(Throwable exception) {

		if (exception instanceof NegocioException) {
			return (NegocioException) exception;

		} else if (exception.getCause() != null) {
			getNegocioException(exception.getCause());
		}

		return null;
	}
}
