package com.forgeon.membermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.forgeon.membermanagement.dto.Skill;
import com.forgeon.membermanagement.dto.User;
import com.forgeon.membermanagement.security.CustomUserDetails;
import com.forgeon.membermanagement.service.SkillService;
import com.forgeon.membermanagement.service.UserService;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	SkillService skillService;
	
	private Integer geUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		return user.getId();
	}

	@GetMapping("/user/home")
	public String user(Model model) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Integer id = geUserId(); 
		
		List<User> userList = userService.getUsers();
		List<Skill> mySkillList = skillService.getMySkills(id);
		
		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}
		
		if (mySkillList.isEmpty()) {
			String noneS = "スキルは未登録です。　";
			model.addAttribute("dataS", noneS);
		}
		
		model.addAttribute("userList", userList);
		model.addAttribute("myskilllist",mySkillList);
		model.addAttribute("username", auth.getName());
		model.addAttribute("skill", new Skill());

		return "user";
	}
	
	@GetMapping("/users/users/search")
	public String usersearch(@RequestParam("uKeyword") String uKeyword,Model model) {
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		Integer id = geUserId(); 
		
		List<User> userList = userService.searchUsers(uKeyword);
		List<Skill> mySkillList = skillService.getMySkills(id);
		
		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}
		
		if (mySkillList.isEmpty()) {
			String noneS = "スキルは未登録です。　";
			model.addAttribute("dataS", noneS);
		}
		
		model.addAttribute("userList", userList);
		model.addAttribute("myskilllist",mySkillList);
		model.addAttribute("username", auth.getName());
		model.addAttribute("skill", new Skill());

		return "user";
	}
	
	@PostMapping("/user/skill/insert")
	public String skillInsert(@ModelAttribute Skill skill, RedirectAttributes redirectAttributes) {
		
		skill.setUserId(geUserId());
		
		String message = skillService.insertSkill(skill);
		
		redirectAttributes.addFlashAttribute("messageS",message);
		
		return "redirect:/user/home";
		
	}
	
	@PostMapping("/user/skill/delete")
	public String skillDelete(@RequestParam("id") Integer id,RedirectAttributes redirectAttributes){

		String message = skillService.deleteMySkill(id);
		
		redirectAttributes.addFlashAttribute("messageS",message);
		
		return "redirect:/user/home";
		
	}

}
