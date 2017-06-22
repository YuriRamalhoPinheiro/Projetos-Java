package com.algaworks.pedidovenda.util.relatorio;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.jdbc.Work;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class ExecutorRelatorio implements Work {

	private String nomeDoArquivoDeSaida;
	private String caminhoDoRelatorio;
	private HttpServletResponse response;
	private Map<String, Object> parametros;
	private boolean relatorioGerado;

	public ExecutorRelatorio(String nomeDoArquivoDeSaida, String caminhoDoRelatorio, HttpServletResponse response,
			Map<String, Object> parametros) {
		this.nomeDoArquivoDeSaida = nomeDoArquivoDeSaida;
		this.caminhoDoRelatorio = caminhoDoRelatorio;
		this.response = response;
		this.parametros = parametros;
	}

	public void execute(Connection connection) throws SQLException {
		try {
			// localiza o arquivo gerado pelo Ireport: relatorio_pedidos.jasper
			InputStream relatorioStream = this.getClass().getResourceAsStream(caminhoDoRelatorio);

			// configura o relatório, define os parametros de entrada e a
			// conexão com o banco de dados
			JasperPrint print = JasperFillManager.fillReport(relatorioStream, this.parametros, connection);

			// verifica se o relatório possui pelo menos uma página
			this.relatorioGerado = print.getPages().size() > 0;

			// configura como o relatório será exibido para o usuário, nesta caso no formato PDF
			Exporter<ExporterInput, PdfReportConfiguration, PdfExporterConfiguration, OutputStreamExporterOutput> exportador = new JRPdfExporter();
			exportador.setExporterInput(new SimpleExporterInput(print));
			exportador.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));

			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + this.nomeDoArquivoDeSaida + "\"");

			// gera o relatório
			exportador.exportReport();
			
		/*	Session session = new EntityManagerProducer().getSessionFromHibernate();
			session.doWork(this);
			*/
		} catch (Exception exception) {
			throw new SQLException("Erro ao executar relatório " + this.caminhoDoRelatorio, exception);
		}

	}

	public boolean isRelatorioGerado() {
		return this.relatorioGerado;
	}

}
