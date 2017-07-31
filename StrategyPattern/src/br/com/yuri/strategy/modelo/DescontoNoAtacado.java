package br.com.yuri.strategy.modelo;

public class DescontoNoAtacado implements CalculadoraDeDesconto {

	@Override
	public Double calcular(Double valor) {
		return valor * 0.10;
	}

}
