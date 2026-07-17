package com.forgeon.todo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.forgeon.todo.security.CustomUserDetails;

@Controller
public class HomeController {
	
	@GetMapping("/home")
	public String home(@AuthenticationPrincipal CustomUserDetails loginUser,Model model) {
		
		String loginName = loginUser.getUser().getName();
		
		model.addAttribute("loginName", loginName);
		
		return "home/home";
	}

}
