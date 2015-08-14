package com.ascendaz.roster.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ascendaz.roster.model.RosterUser;
import com.ascendaz.roster.model.config.Criteria;
import com.ascendaz.roster.model.config.Rule;
import com.ascendaz.roster.model.config.RuleType;
import com.ascendaz.roster.model.config.SetupOption;
import com.ascendaz.roster.service.ConfigService;
import com.ascendaz.roster.service.UserService;


@Controller("configController")

public class ConfigController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = "config")
	public String configPage(Model model){
		System.out.println("Config Controller");
		
		List<SetupOption> selectedSetupOptions = configService.getSelectedSetupOptions();
		model.addAttribute("selectedSetupOptions", selectedSetupOptions);
		
		Criteria[] criteriaList = configService.getCriteriaList();
		model.addAttribute("criteriaList", criteriaList);
		
		RuleType[] typeList = configService.getRuleTypeList();
		model.addAttribute("typeList", typeList);
		
		return "config";
	}

	@RequestMapping(value = "config/login", method = RequestMethod.POST)
	public @ResponseBody String loginAJAX(@RequestParam(value="username", required=true) String username, 
			@RequestParam(value="password", required=true) String password, HttpSession session) {
		
		System.out.println("username: " + username + " password: " + password);
		
		boolean loginSuccessfull = userService.checkUser(username, password);
		if (loginSuccessfull) {
			session.setAttribute("loginUser", new RosterUser(username, password));
			return "success";
		}
		else {
			return "fail";
		}
	}
	@RequestMapping(value = "/config/available-options", method = RequestMethod.GET)
	public @ResponseBody List<SetupOption> getAvailableOptions() {
		
		List<SetupOption> availableOptions = configService.getAvailableSetupOptions();
		
		return availableOptions;
	}
	
	@RequestMapping(value = "/config/selected-options", method = RequestMethod.GET)
	public @ResponseBody List<SetupOption> getSelectedOptions() {
		
		List<SetupOption> selectedOptions = configService.getSelectedSetupOptions();
		
		return selectedOptions;
	}
	
	@RequestMapping(value = "/config/save-available-options", method = RequestMethod.POST)
	public @ResponseBody String saveAvailableOptions(@RequestBody List<String> options) {
		configService.saveOptions(options, false);
		
		return "success";
	}
	
	@RequestMapping(value = "/config/save-selected-options", method = RequestMethod.POST)
	public @ResponseBody String saveSelectedOptions(@RequestBody List<String> options) {
		configService.saveOptions(options, true);
		
		return "success";
	}
	
	@RequestMapping(value = "/config/selected-rules", method = RequestMethod.GET)
	public @ResponseBody List<Rule> getSelectedRules() {
		List<SetupOption> selectedOptions = configService.getSelectedSetupOptions();
		List<Rule> selectedRules = configService.getSelectedRules(selectedOptions);
		
		for (Rule rule: selectedRules) {
			System.out.println("Controller: RULE: " + rule);
		}
		
		return selectedRules;
	}
	
	@RequestMapping(value = "/config/save-rules", method = RequestMethod.POST)
	public @ResponseBody String saveRules(@RequestBody List<String> rules) {
		
		configService.saveRules(rules);
		
		return "success";
	}
	
}
