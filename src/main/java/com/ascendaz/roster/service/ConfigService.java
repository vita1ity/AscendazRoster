package com.ascendaz.roster.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ascendaz.roster.model.SetupOption;
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
	
	
}
