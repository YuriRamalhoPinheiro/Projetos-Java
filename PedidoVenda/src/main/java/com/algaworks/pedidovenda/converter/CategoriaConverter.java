package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.repository.CategoriaRepository;

/*
Observação: Para injetar objetos com CDI nesta classe é necessário
adicionar a dependência do omnifaces 2.0 no projeto 
*/

@FacesConverter(forClass = Categoria.class)
public class CategoriaConverter implements Converter {
	
	@Inject
	private CategoriaRepository categoriaRepository;
	
	public CategoriaConverter() {
		//this.categoriaRepository = CDIServiceLocator.getBean(CategoriaRepository.class);
	}
	
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String value) {
		Categoria retorno = null;

		if (value != null) {
			System.out.println("Convertendo Categoria para Objeto");
			Long id = new Long(value);
			retorno = categoriaRepository.porId(id);
		}
		
		return retorno;
	}

	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		if (value != null) {
			System.out.println("Convertendo Categoria"+ value +" para String");
			return ((Categoria) value).getId().toString();
		}
		return "";
	}

}
