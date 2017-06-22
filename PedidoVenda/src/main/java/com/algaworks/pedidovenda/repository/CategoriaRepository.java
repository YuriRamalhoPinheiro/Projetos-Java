package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.algaworks.pedidovenda.model.Categoria;

@Named
public class CategoriaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	public List<Categoria> buscarCateogoriasRaizes() {
		return entityManager.createQuery("from Categoria where categoriaPai is null", Categoria.class).getResultList();
	}

	public List<Categoria> buscarSubCategoriasDe(Categoria categoriaPai) {

		return entityManager.createQuery("from Categoria where categoriaPai = :raiz", Categoria.class)
				.setParameter("raiz", categoriaPai).getResultList();

	};

	public Categoria porId(Long id) {
		return entityManager.find(Categoria.class, id);
	}
}
