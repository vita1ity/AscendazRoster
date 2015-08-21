package com.ascendaz.roster.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;
import com.ascendaz.roster.repository.ConfigRepository;

@Service("configService")
public class ConfigService {
	
	@Autowired
	private ConfigRepository configRepository;

	public List<SetupOption> getSelectedSetupOptions() {
		
		return configRepository.getOptionsByIsSelected(true);
	}

	public List<SetupOption> getAvailableSetupOptions() {
		
		return configRepository.getOptionsByIsSelected(false);
	}
	@Transactional
	public void saveOptions(List<String> options, boolean isSelected) {
		for (String option: options) {
			configRepository.setSelected(option, isSelected);
		}
	}

	public List<Rule> getSelectedRules(List<SetupOption> selectedOptions) {
		List<Rule> resultList = new ArrayList<Rule>();
		Rule rule = null;
		for (SetupOption option: selectedOptions) {
			rule = configRepository.getRuleByOption(option.getId());
			resultList.add(rule);
		}
		Collections.sort(resultList);
		return resultList;
	}

	public Criteria[] getCriteriaList() {
		Criteria[] criterias = Criteria.class.getEnumConstants();
		return criterias;
	}

	public RuleType[] getRuleTypeList() {
		RuleType[] types = RuleType.class.getEnumConstants();
		return types;
		
	}
	@Transactional
	public void saveRules(List<String> rules) {
		int priority = 1;
		configRepository.setAllRulesNotSelected();
		for (String rule: rules) {
			
			rule = rule.replaceAll("\n", "");
			//System.out.println(rule);
			String[] ruleArgs = rule.split(",");
			String base = ruleArgs[0];
			
			Criteria criteria = Criteria.constructCriteria(ruleArgs[1]);
			String reference = ruleArgs[2];
			RuleType type = RuleType.constructType(ruleArgs[3]);
			while (base.startsWith(" ")) {
				//System.out.println(base);
				base = base.substring(1);
			}
			while (reference.startsWith(" ")) {
				
				reference = reference.substring(1);
			}
			//System.out.println("Base: " + base + ", criteria: " + criteria + ", reference: " + reference + " , type: " + type);
			configRepository.updateRule(base, criteria, reference, type, priority);
			priority++;
		}
		
	}
	
	
}
