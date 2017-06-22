package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.model.vo.DataValor;
import com.algaworks.pedidovenda.repository.filter.PedidoFilter;

public class PedidoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;
	
	public Map<Date, BigDecimal> valoresTotaisPorData(Integer numeroDeDias, Usuario criadoPor) {
		//Session session = entityManager.unwrap(Session.class);
		
		numeroDeDias -= 1;
		
		Calendar dataInicial = Calendar.getInstance();
		dataInicial = DateUtils.truncate(dataInicial, Calendar.DAY_OF_MONTH);
		dataInicial.add(Calendar.DAY_OF_MONTH, (numeroDeDias * -1));

		Map<Date, BigDecimal> mapa = criarMapaVazio(numeroDeDias, dataInicial);
		
		String jpql = "select new com.algaworks.pedidovenda.model.vo.DataValor(date(p.dataCriacao), sum(p.valorTotal))"
				+ "from Pedido p where p.dataCriacao >= :dataInicial ";
		
		if (criadoPor != null) {
			jpql += "and p.vendedor = :vendedor ";
		}
		
		jpql += "group by date(dataCriacao)";
		
		TypedQuery<DataValor> query = entityManager.createQuery(jpql, DataValor.class);
		
		query.setParameter("dataInicial", dataInicial.getTime());
		
		if (criadoPor != null) {
			query.setParameter("vendedor", criadoPor);
		}
		
		List<DataValor> valoresPorData = query.getResultList();
		
		for (DataValor dataValor : valoresPorData) {
			mapa.put(dataValor.getData(), dataValor.getValor());
		}
		
		//@SuppressWarnings("deprecation")
		//Criteria criteria = session.createCriteria(Pedido.class);
		
		/*
		 * select date(data_criacao) as data, sum(valor_total) as valor 
		 * from pedido where data_criacao >= :dataInicial and vendedor_id = :criadoPor
		 * group by date(data_criacao) */
		
		/*Projection sqlGroupProjection = Projections.sqlGroupProjection("date(data_criacao) as data",
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
		*/
		return mapa;
	}

	private Map<Date, BigDecimal> criarMapaVazio(Integer numeroDeDias, Calendar dataInicial) {
		dataInicial = (Calendar) dataInicial.clone();

		Map<Date, BigDecimal> mapaInicial = new TreeMap<Date, BigDecimal>();

		for (int indice = 0; indice <= numeroDeDias; indice++) {
			mapaInicial.put(dataInicial.getTime(), BigDecimal.ZERO);
			dataInicial.add(Calendar.DAY_OF_MONTH, 1);
		}

		return mapaInicial;
	}
	
	private List<Predicate> criarPredicatesParaFiltro(PedidoFilter filtro, Root<Pedido> pedidoRoot,
			From<?, ?> clienteJoin, From<?, ?> vendedorJoin) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicates = new ArrayList<Predicate>();

		if (filtro.getNumeroDe() != null) {
			// id deve ser maior ou igual (ge greater or equals) a
			// filtro.getNumeroDe()
			predicates.add(builder.greaterThanOrEqualTo(pedidoRoot.<Long>get("id"), filtro.getNumeroDe()));
		}

		if (filtro.getNumeroAte() != null) {
			// id deve ser menor ou igual (lower or equals) a
			// filtro.getNumeroAte()
			predicates.add(builder.lessThanOrEqualTo(pedidoRoot.<Long>get("id"), filtro.getNumeroAte()));
		}

		if (filtro.getDataCriacaoDe() != null) {
			predicates
					.add(builder.greaterThanOrEqualTo(pedidoRoot.<Date>get("dataCriacao"), filtro.getDataCriacaoDe()));
		}

		if (filtro.getDataCriacaoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(pedidoRoot.<Date>get("dataCriacao"), filtro.getDataCriacaoAte()));
		}

		if (StringUtils.isNotBlank(filtro.getNomeCliente())) {

			// acessamos o nome do cliente associado ao pedido pela alias
			// "aliasNome", anteriormente criada
			predicates.add(builder.like(clienteJoin.<String>get("nome"), "%" + filtro.getNomeCliente() + "%"));
		}

		if (StringUtils.isNotBlank(filtro.getNomeVendedor())) {

			// acessamos o nome do vendedor associado ao pedido pela alias
			// "aliasNome", anteriormente criada
			predicates.add(builder.like(vendedorJoin.<String>get("nome"), "%" + filtro.getNomeVendedor() + "%"));
		}

		if (filtro.getStatuses() != null && filtro.getStatuses().length > 0) {
			predicates.add(pedidoRoot.<String>get("status").in(Arrays.asList(filtro.getStatuses())));
		}

		return predicates;
	}

	public List<Pedido> filtrados(PedidoFilter filtro) {

		From<?, ?> orderByFromEntity = null;

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Pedido> criteriaQuery = builder.createQuery(Pedido.class);

		Root<Pedido> pedidoRoot = criteriaQuery.from(Pedido.class);
		From<?, ?> clienteJoin = (From<?, ?>) pedidoRoot.fetch("cliente", JoinType.INNER);
		From<?, ?> vendedorJoin = (From<?, ?>) pedidoRoot.fetch("vendedor", JoinType.INNER);

		List<Predicate> predicates = criarPredicatesParaFiltro(filtro, pedidoRoot, clienteJoin, vendedorJoin);

		criteriaQuery.select(pedidoRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		if (filtro.getPropriedadeOrdenacao() != null) {
			String nomePropriedadeOrdenacao = filtro.getPropriedadeOrdenacao();
			orderByFromEntity = pedidoRoot;

			if (filtro.getPropriedadeOrdenacao().contains(".")) {
				nomePropriedadeOrdenacao = nomePropriedadeOrdenacao
						.substring(filtro.getPropriedadeOrdenacao().indexOf(".") + 1);
			}

			if (filtro.getPropriedadeOrdenacao().startsWith("cliente.")) {
				orderByFromEntity = clienteJoin;
			}

			if (filtro.isAscendente() && filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.asc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			} else if (filtro.getPropriedadeOrdenacao() != null) {
				criteriaQuery.orderBy(builder.desc(orderByFromEntity.get(nomePropriedadeOrdenacao)));
			}
		}

		TypedQuery<Pedido> query = entityManager.createQuery(criteriaQuery);

		query.setFirstResult(filtro.getPrimeiroRegistro());
		query.setMaxResults(filtro.getQuantidadeRegistros());

		return query.getResultList();
	}

	public Pedido guardar(Pedido pedido) {
		return entityManager.merge(pedido);
	}
	
	public Pedido porId(long id){
		return this.entityManager.find(Pedido.class, id);
	}
}
