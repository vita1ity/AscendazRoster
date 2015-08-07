package com.ascendaz.roster.repository;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;


@Repository("configRepository")
public class ConfigRepository {
	
	/*@PersistenceContext
	private EntityManager em;*/
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<SetupOption> getOptionsByIsSelected(boolean isSelected) {
		/*TypedQuery<SetupOption> query = em.createNamedQuery(SetupOption.GET_OPTIONS_BY_IS_SELECETED, SetupOption.class);
		query.setParameter("isSelected", isSelected);
		List<SetupOption> options = query.getResultList();
		
		return options;
		*/
		String hqlQuery =  "FROM SetupOption "
						+ "WHERE isSelected = :isSelected";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("isSelected", isSelected);
		List<SetupOption> res = query.list();
		return res;
	}
	
	public void setSelected(String setupOption, boolean isSelected) {
		/*Query query = em.createQuery("UPDATE SetupOption option "
									+ "SET option.isSelected = :isSelected "
									+ "WHERE option.setupOption = :setupOption");
		query.setParameter("isSelected", isSelected);
		query.setParameter("setupOption", option);
		int rows = query.executeUpdate();
		*/
		
		String hqlQuery =  "UPDATE SetupOption so "
						+ "SET so.isSelected = :isSelected "
						+ "WHERE so.setupOption = :setupOption";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("isSelected", isSelected);
		query.setParameter("setupOption", setupOption);
		int rows = query.executeUpdate();
		
	}
	
	public Rule getRuleByOption(int setupOptionId) {
		/*TypedQuery<Rule> query = em.createNamedQuery(Rule.GET_RULE_BY_OPTION, Rule.class);
		query.setParameter("setupOptionId", setupOptionId);
		Rule rule = query.getSingleResult();
		*/
		String hqlQuery = "FROM Rule "
						+ "WHERE setupOption.id = :setupOptionId";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("setupOptionId", setupOptionId);
		Rule rule = (Rule)query.uniqueResult();
		
		return rule;
	}

	public void updateRule(String base, Criteria criteria, String reference, RuleType type, int priority) {
		/*Query query = em.createQuery("UPDATE Rule rule1 "
				+ "SET rule1.criteria = :criteria, rule1.type = :type, rule1.priority = :priority, rule1.isSelected = true "
				
				+ "WHERE rule1.setupOption.setupOption = :base");*/
		//Query query = em.createNativeQuery(Rule.SAVE_RULE);
		/*Query query = em.createNamedQuery(Rule.SAVE_RULE);
		
		
		query.setParameter("criteria", criteria.ordinal());
		query.setParameter("type", type.ordinal());
		query.setParameter("priority", priority);
		query.setParameter("base", base);
		int rows = query.executeUpdate();
		System.out.println(rows + " rules updated");*/
		
		String hqlQuery = "UPDATE Rule r "
						+ "SET r.criteria = :criteria, r.type = :type, r.priority = :priority, r.isSelected = true "
						+ "WHERE r.setupOption.setupOption = :base";
		String sqlQuery = "UPDATE roster_rule "
						+ "SET CRITERIA = :criteria, TYPE = :type, PRIORITY = :priority, IS_SELECTED = 1 "
						+ "WHERE SETUP_OPTION_ID = (SELECT OPTION_ID "
												+ "FROM setup_option "
												+ "WHERE SETUP_OPTION = :base)";
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sqlQuery);
		query.setParameter("criteria", criteria.ordinal());
		query.setParameter("type", type.ordinal());
		query.setParameter("priority", priority);
		query.setParameter("base", base);
		int rows = query.executeUpdate();
		System.out.println(rows + " rules updated");
		
	}

	public void setAllRulesNotSelected() {
		
		/*Query query = em.createQuery("UPDATE Rule rule1 "
								+ "SET rule1.isSelected = false");
		int rows = query.executeUpdate();*/
		
		String hqlQuery = "UPDATE Rule r "
						+ "SET r.isSelected = false";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		int rows = query.executeUpdate();
		
	}
}
