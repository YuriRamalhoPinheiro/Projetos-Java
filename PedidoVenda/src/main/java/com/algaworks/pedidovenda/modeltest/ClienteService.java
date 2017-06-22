package com.algaworks.pedidovenda.modeltest;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.model.TipoPessoa;

public class ClienteService {
	
	public Cliente criarCliente(){
		
		Cliente cliente = new Cliente();
		cliente.setNome("Ana Flavia");
		cliente.setEmail("perigoso@hotmail.com");
		cliente.setTipo(TipoPessoa.FISICA);
		cliente.setDocumentoFederal("000.343.456-88");
				
		Endereco endereco = new Endereco();	
		endereco.setLogradouro("Rua do Meio");
		endereco.setNumero("120");
		endereco.setComplemento("Casa");
		endereco.setCidade("Araçuaí");
		endereco.setCep("39600-000");
		endereco.setUf("MG");
		endereco.setCliente(cliente);

		cliente.getEndereco().add(endereco);
		
		return cliente;
	}
	
}
