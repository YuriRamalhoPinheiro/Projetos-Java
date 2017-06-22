import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.hibernate.Session;

import com.algaworks.pedidovenda.util.jpa.EntityManagerProducer;
import com.algaworks.pedidovenda.util.relatorio.ExecutorRelatorio;

@Named("relatorioPedidosEmitidosBean")
@RequestScoped
public class RelatorioPedidosEmitidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date dataInicio;
	private Date dataFim;
	private String nomeDoArquivoDeSaida;
	private String caminhoDoRelatorio;
	private Map<String, Object> parametros;
	
	@Inject
	private FacesContext facesContext;
	@Inject
	private HttpServletResponse response;
	
	public RelatorioPedidosEmitidosBean() {
		this.caminhoDoRelatorio = "/relatorios/relatorio_pedidos.jasper";
		this.nomeDoArquivoDeSaida = "Pedidos emitidos.pdf";
		parametros = new HashMap<String, Object>();
	}
	
	public String emitir() {
		
		parametros.put("DATA_INICIO", this.dataInicio);
		parametros.put("DATA_FIM", this.dataFim);
		
		ExecutorRelatorio executorRelatorio = new ExecutorRelatorio(nomeDoArquivoDeSaida, caminhoDoRelatorio,
				this.response, parametros);

		Session session = new EntityManagerProducer().getSessionFromHibernate();
		session.doWork(executorRelatorio);
		
		//finaliza o ciclo de vida da requisição da página JSF
		this.facesContext.responseComplete();
		
		return "";
	}
	
	// Getters e Setters

	@NotNull
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	@NotNull
	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
