package com.algaworks.pedidovenda.modeltest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.algaworks.pedidovenda.model.Cliente;

public class clienteTeste {
	
	
	public static void main(String[] args) {

		Cliente cliente = new ClienteService().criarCliente();

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PUPedidoVenda");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();

		entityManager.persist(cliente);
		// entityManager.remove(arg0);

		transaction.commit();

		entityManager.close();
	}
}
