package com.algaworks.pedidovenda.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

@ApplicationScoped
public class EntityManagerProducer {
	
	private EntityManagerFactory entityManagerFactory;
	
	public EntityManagerProducer() {
	
		entityManagerFactory = Persistence.createEntityManagerFactory("PUPedidoVenda");
	}
	
	@Produces @RequestScoped
	public EntityManager createEntityManager(){
		return entityManagerFactory.createEntityManager();
	}
	
	public void closeEntityManager(@Disposes EntityManager manager){
		manager.close();
	}
	
	public Session getSessionFromHibernate(){
		EntityManager entityManager = createEntityManager();
		Session session = (Session) entityManager.getDelegate();
		return session;
	}
}
