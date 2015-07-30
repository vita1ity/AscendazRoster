package com.ascendaz.roster.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ascendaz.roster.model.RosterUser;
import com.ascendaz.roster.service.UserService;

@Controller("configController")

public class ConfigController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "config")
	public String configPage(Model model){
		RosterUser user = new RosterUser();
		model.addAttribute("user", user);
		System.out.println("Config Controller");
		return "config";
	}
	@RequestMapping(value = "config/login", method = RequestMethod.POST)
	public String login(@ModelAttribute("user") RosterUser user, Model model, HttpSession session) {
		boolean loginSuccessfull = userService.checkUser(user);
		if (loginSuccessfull) {
			session.setAttribute("loginUser", user);
			return "redirect:/config";
		}
		else {
			model.addAttribute("loginError", true);
			return "config";
		}
	}
}
