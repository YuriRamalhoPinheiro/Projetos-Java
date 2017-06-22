package com.algaworks.pedidovenda.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.repository.PedidoRepository;

@FacesConverter(forClass = Pedido.class)
public class PedidoConverter implements Converter {

	@Inject
	PedidoRepository pedidoRepository;

	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (StringUtils.isNotEmpty(value)) {
			long id = Long.parseLong(value);
			return this.pedidoRepository.porId(id);
		}
		return null;
	}

	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (value != null) {
			Pedido pedido = (Pedido) value;
			return pedido.getId() == null ? null : pedido.getId().toString();
		}
		return "";
	}

}
