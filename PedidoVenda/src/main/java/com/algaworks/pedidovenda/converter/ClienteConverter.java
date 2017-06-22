package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.ClienteRepository;

/*
 Observação: Para injetar objetos com CDI nesta classe é necessário
 adicionar a dependência do omnifaces 2.0 no projeto 
 */

@FacesConverter(forClass = Cliente.class)
public class ClienteConverter implements Converter {

	@Inject
	private ClienteRepository clienteRepository;

	public ClienteConverter() {
		// this.produtoRepository =
		// CDIServiceLocator.getBean(ProdutoRepository.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Cliente cliente = null;

		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			cliente = this.clienteRepository.porId(id);
		}

		return cliente;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			Cliente cliente = (Cliente) value;
			return cliente.getId() == null ? null : cliente.getId().toString();
		}

		return "";
	}

}
