package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.UsuarioRepository;

/*
 Observação: Para injetar objetos com CDI nesta classe é necessário
 adicionar a dependência do omnifaces 2.0 no projeto 
 */

@FacesConverter(forClass = Usuario.class)
public class UsuarioConverter implements Converter {

	@Inject
	private UsuarioRepository usuarioRepository;

	public UsuarioConverter() {
		// this.produtoRepository =
		// CDIServiceLocator.getBean(ProdutoRepository.class);
	}

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Usuario usuario = null;

		if (StringUtils.isNotEmpty(value)) {
			Long id = new Long(value);
			usuario = this.usuarioRepository.porId(id);
		}

		return usuario;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			Usuario usuario = (Usuario) value;
			return usuario.getId() == null ? null : usuario.getId().toString();
		}

		return "";
	}

}
