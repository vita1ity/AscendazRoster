package com.ascendaz.roster.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.ascendaz.roster.model.SetupOption;

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
}