package com.forgeon.membermanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping("/")
	public String start() {
		return "redirect:/top";
	}
	
	@GetMapping("/top")
	public String top() {
		return "top";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
