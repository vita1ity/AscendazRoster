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
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public List<SetupOption> getOptionsByIsSelected(boolean isSelected) {
		
		String hqlQuery =  "FROM SetupOption "
						+ "WHERE isSelected = :isSelected";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("isSelected", isSelected);
		List<SetupOption> res = query.list();
		return res;
	}
	
	public void setSelected(String setupOption, boolean isSelected) {
		
		String hqlQuery =  "UPDATE SetupOption so "
						+ "SET so.isSelected = :isSelected "
						+ "WHERE so.setupOption = :setupOption";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("isSelected", isSelected);
		query.setParameter("setupOption", setupOption);
		int rows = query.executeUpdate();
		
	}
	
	public Rule getRuleByOption(int setupOptionId) {
		
		String hqlQuery = "FROM Rule "
						+ "WHERE setupOption.id = :setupOptionId";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		query.setParameter("setupOptionId", setupOptionId);
		Rule rule = (Rule)query.uniqueResult();
		
		return rule;
	}

	public void updateRule(String base, Criteria criteria, String reference, RuleType type, int priority) {
		
		
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
		
		String hqlQuery = "UPDATE Rule r "
						+ "SET r.isSelected = false";
		Query query = sessionFactory.getCurrentSession().createQuery(hqlQuery);
		int rows = query.executeUpdate();
		
	}
}
