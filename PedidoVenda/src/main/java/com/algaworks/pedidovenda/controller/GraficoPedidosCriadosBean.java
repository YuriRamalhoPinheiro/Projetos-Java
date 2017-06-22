package com.algaworks.pedidovenda.controller;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;

import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.PedidoRepository;
import com.algaworks.pedidovenda.security.UsuarioLogado;
import com.algaworks.pedidovenda.security.UsuarioSistema;

@Named("graficoPedidosCriadosBean")
@RequestScoped
public class GraficoPedidosCriadosBean {
	
	//private LineChartModel modeloCartesiano;
	private BarChartModel modeloCartesiano;
	private static DateFormat FORMATO_DATA = new SimpleDateFormat("dd/MM");
	
	@Inject
	private PedidoRepository pedidoRepository;
	@Inject @UsuarioLogado
	private UsuarioSistema usuarioLogado;
	
	/** este método será chamado na inicialização da página home.xhtml,
	 * ele define as configurações do gráfico */
	public void preRender() {
		//this.modeloCartesiano = new LineChartModel();
		this.modeloCartesiano = new BarChartModel();
		this.modeloCartesiano.setTitle("Gráfico de Pedidos");
		this.modeloCartesiano.setLegendPosition("e");
		this.modeloCartesiano.setAnimate(true);
		this.modeloCartesiano.getAxes().put(AxisType.X, new CategoryAxis());
		this.modeloCartesiano.getAxis(AxisType.X).setLabel("Datas das vendas");
		this.modeloCartesiano.getAxis(AxisType.Y).setLabel("Valores das vendas");
		
		/*Axis axisX = this.modeloCartesiano.getAxis(AxisType.X);
		axisX.setMin(0);
		axisX.setMax(60); */ 
		
		this.adicionarSeries("Todos os pedidos", null);
		this.adicionarSeries("meus pedidos", this.usuarioLogado.getUsuario());
	}
	
	/** este método adiciona os valores(series) no gráfico*/
	private void adicionarSeries(String rotulo, Usuario criadoPor) {
		Map<Date, BigDecimal> valoresPorData = this.pedidoRepository.valoresTotaisPorData(10, criadoPor);
		
		//LineChartSeries series = new LineChartSeries();
		ChartSeries series = new ChartSeries();
		series.setLabel(rotulo);
		
		for(Date data : valoresPorData.keySet()){
			series.set(FORMATO_DATA.format(data), valoresPorData.get(data));
		}

		this.modeloCartesiano.addSeries(series);
	}
	
	//getters e setters
	
	public BarChartModel getGraficoModelo() {
		return modeloCartesiano;
	}

	public void setGraficoModelo(BarChartModel graficoModelo) {
		this.modeloCartesiano = graficoModelo;
	}

}
