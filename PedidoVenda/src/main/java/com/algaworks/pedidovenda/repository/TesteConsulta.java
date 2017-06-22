package com.algaworks.pedidovenda.repository;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.model.vo.DataValor;

public class TesteConsulta {

	public static void main(String... args) {
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("PUPedidoVenda");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		
		Map<Date, BigDecimal> valores = valoresTotaisPorData(60, usuario, session);
		
		for(Date data : valores.keySet()){
			System.out.println(data + "=" + valores.get(data));
		}
		
		entityManager.close();
	}

	public static Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor, Session session) {
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, (numeroDeDias * -1));

		Map<Date, BigDecimal> mapa = criarMapaVazio(numeroDeDias, dataInicial);
		
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(Pedido.class);

		Projection sqlGroupProjection = Projections.sqlGroupProjection("date(data_criacao) as data",
				"date(data_criacao)", new String[] { "data" }, new Type[] { StandardBasicTypes.DATE });

		ProjectionList projectionList = Projections.projectionList().add(sqlGroupProjection)
																	.add(Projections.sum("valorTotal").as("valor"));
		
		SimpleExpression restriction = Restrictions.ge("dataCriacao", dataInicial.getTime());
		
		criteria.setProjection(projectionList).add(restriction);

		if(criadoPor != null){
			criteria.add(Restrictions.eq("vendedor", criadoPor));
		}
		
		@SuppressWarnings("unchecked")
		List<DataValor> valoresPorData = criteria.setResultTransformer(Transformers.aliasToBean(DataValor.class)).list();
		
		for(DataValor dataValor : valoresPorData){
			mapa.put(dataValor.getData(), dataValor.getValor());
		}
		
		return mapa;
	}

	private static Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();

		Map<Date, BigDecimal> mapaInicial = new TreeMap<Date, BigDecimal>();

		for (int indice = 0; indice <= numeroDeDias; indice++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}
}
