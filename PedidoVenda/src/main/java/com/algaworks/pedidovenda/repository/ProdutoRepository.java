package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.filter.ProdutoFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class ProdutoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	@Transactional
	public Produto guardar(Produto produto) {
		entityManager.merge(produto); // adiciona ou atualizado no banco de dados

		return produto;
	}
	
	@Transactional
	public void excluir(Produto produto){
		try{
			produto = porId(produto.getId());
			entityManager.remove(produto);
			entityManager.flush();
		}catch(PersistenceException exception){
			throw new NegocioException("Produto não pode ser excluído!");
		}
	}
		
	public Produto porSku(String sku) {
		try {
			return entityManager.createQuery("from Produto where upper(sku) = :sku", Produto.class)
					.setParameter("sku", sku.toUpperCase()).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	
	
	public Produto porId(Long id) {
		Long idProduto = id;
		
		return this.entityManager.find(Produto.class, idProduto);
	}
	
	public List<Produto> filtrados(ProdutoFilter filtro) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Produto> criteriaQuery = builder.createQuery(Produto.class);
		
		Root<Produto> produtoRoot = criteriaQuery.from(Produto.class);
		Fetch<Produto, Categoria> categoriaJoin = produtoRoot.fetch("categoria", JoinType.INNER);
		categoriaJoin.fetch("categoriaPai", JoinType.INNER);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (StringUtils.isNotBlank(filtro.getSku())) {
			predicates.add(builder.equal(produtoRoot.get("sku"), filtro.getSku()));
		}
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			predicates.add(builder.like(builder.lower(produtoRoot.<String>get("nome")), 
					"%" + filtro.getNome().toLowerCase() + "%"));
		}
		
		criteriaQuery.select(produtoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(produtoRoot.get("nome")));
		

		TypedQuery<Produto> query = entityManager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Produto> porNome(String nome) {
		 return this.entityManager.createQuery("from Produto where upper(nome) like :nome", Produto.class)
							 		.setParameter("nome", nome.toUpperCase() + "%")
							 		.getResultList();
	}
	
}
