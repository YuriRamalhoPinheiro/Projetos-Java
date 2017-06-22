package com.algaworks.pedidovenda.controller;

import java.util.ArrayList;
import javax.enterprise.inject.Produces;
import com.algaworks.pedidovenda.service.qualificadores_cdi.TipoArrayList;

/**
 * @author Yuri
 * @see TipoArrayList 
 * @version 1.0 
 * Classe que produz instâncias de objeto objetos do tipo {ArrayList}, através de CDI,  para 
 * serem injetados nos beans CDI*/
public class TiposDeListas {
	
	/**
	 * @return retorna uma instância do tipo <code>ArrayList</code>*/
	@Produces @TipoArrayList
	public ArrayList<Integer> getArrayList(){
		return new ArrayList<Integer>();
	}
}
