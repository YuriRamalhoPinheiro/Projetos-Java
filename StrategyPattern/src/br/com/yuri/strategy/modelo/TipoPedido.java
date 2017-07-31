package br.com.yuri.strategy.modelo;

/**
 * retorna um tipo de desconto a ser aplicado, 
 * conforme o tipo da enum escolhida
 * 
 * @see CalculadoraDeDesconto, DescontoNoVarejo, DescontoNoAtacado
 * @return CalculadoraDeDesconto
 */
public enum TipoPedido {

	VAREJO {
		@Override
		public CalculadoraDeDesconto calculadora() {
			return new DescontoNoVarejo();
		}
	},

	ATACADO {
		@Override
		public CalculadoraDeDesconto calculadora() {
			return new DescontoNoAtacado();
		}

	};

	public abstract CalculadoraDeDesconto calculadora();

}