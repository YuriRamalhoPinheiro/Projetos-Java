package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.ProdutoRepository;

/*
 Observação: Para injetar objetos com CDI nesta classe é necessário
 adicionar a dependência do omnifaces 2.0 no projeto 
 */

@FacesConverter(forClass = Produto.class)
public class ProdutoConverter implements Converter {

	@Inject
	private ProdutoRepository produtoRepository;

	public ProdutoConverter() {
		// this.produtoRepository =
		// CDIServiceLocator.getBean(ProdutoRepository.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Produto produto = null;

		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			produto = this.produtoRepository.porId(id);
		}

		return produto;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			Produto produto = (Produto) value;
			return produto.getId() == null ? null : produto.getId().toString();
		}

		return "";
	}

}
