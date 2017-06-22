package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.algaworks.pedidovenda.model.Usuario;

public class UsuarioRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public Usuario porId(Long id) {
		return this.entityManager.find(Usuario.class, id);
	}

	public List<Usuario> buscarVendedores() {
		// TODO filtar apenas vendedores ( por um grupo especifico )
		return this.entityManager.createQuery("from Usuario", Usuario.class).getResultList();
	}

	public Usuario porEmail(String email) {
		Usuario usuario = null;
		try {

			usuario = this.entityManager.createQuery("from Usuario where lower(email) = :email", Usuario.class)
					.setParameter("email", email.toLowerCase()).getSingleResult();
		} catch (NoResultException exception) {
			exception.getMessage();
			exception.getCause();
			//nenhum usuario encontrado com o email informado!
		}

		return usuario;
	}

}
