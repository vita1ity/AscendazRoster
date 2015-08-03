package com.ascendaz.roster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;


@Repository("configRepository")
public class ConfigRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public List<SetupOption> getOptionsByIsSelected(boolean isSelected) {
		TypedQuery<SetupOption> query = em.createNamedQuery(SetupOption.GET_OPTIONS_BY_IS_SELECETED, SetupOption.class);
		query.setParameter("isSelected", isSelected);
		List<SetupOption> options = query.getResultList();
		
		return options;
	}
	
	public void setSelected(String option, boolean isSelected) {
		Query query = em.createQuery("UPDATE SetupOption option "
									+ "SET option.isSelected = :isSelected "
									+ "WHERE option.setupOption = :setupOption");
		//TypedQuery<SetupOption> query = null;
		/*if (isSelected) {
			query = em.createNamedQuery(SetupOption.SET_SELECTED_TRUE, SetupOption.class);
		}
		else {
			query = em.createNamedQuery(SetupOption.SET_SELECTED_FALSE, SetupOption.class);
		}*/
		query.setParameter("isSelected", isSelected);
		query.setParameter("setupOption", option);
		int rows = query.executeUpdate();
	}
	
	public Rule getRuleByOption(int setupOptionId) {
		TypedQuery<Rule> query = em.createNamedQuery(Rule.GET_RULE_BY_OPTION, Rule.class);
		query.setParameter("setupOptionId", setupOptionId);
		Rule rule = query.getSingleResult();
		/*List<Rule> rules = query.getResultList();
		for (Rule rule: rules) {
			System.out.println("Rule" + rule);
		}*/
		return rule;
	}

	public void updateRule(String base, Criteria criteria, String reference, RuleType type, int priority) {
		/*Query query = em.createQuery("UPDATE Rule rule1 "
				+ "SET rule1.criteria = :criteria, rule1.type = :type, rule1.priority = :priority, rule1.isSelected = true "
				
				+ "WHERE rule1.setupOption.setupOption = :base");*/
		//Query query = em.createNativeQuery(Rule.SAVE_RULE);
		Query query = em.createNamedQuery(Rule.SAVE_RULE);
		
		
		query.setParameter("criteria", criteria.ordinal());
		query.setParameter("type", type.ordinal());
		query.setParameter("priority", priority);
		query.setParameter("base", base);
		int rows = query.executeUpdate();
		System.out.println(rows + " rules updated");
	}

	public void setAllRulesNotSelected() {
		Query query = em.createQuery("UPDATE Rule rule1 "
								+ "SET rule1.isSelected = false");
		int rows = query.executeUpdate();
	}
}
