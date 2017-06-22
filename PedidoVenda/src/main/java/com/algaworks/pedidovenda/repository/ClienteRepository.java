package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.Cliente;

public class ClienteRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public Cliente porId(Long id) {
		return entityManager.find(Cliente.class, id);
	}

	public List<Cliente> porNome(String nome) {
		final String QUERY = "from Cliente where upper(nome) like :nome";
		String parametroNome = nome.toUpperCase() + "%";
		
		return this.entityManager.createQuery(QUERY, Cliente.class).setParameter("nome", parametroNome).getResultList();
	}

}
