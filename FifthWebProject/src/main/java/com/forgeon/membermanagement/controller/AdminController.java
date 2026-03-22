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
import com.forgeon.membermanagement.service.SkillService;
import com.forgeon.membermanagement.service.UserService;

@Controller
public class AdminController {

	@Autowired
	UserService userService;

	@Autowired
	SkillService skillService;

	@GetMapping("/admin/home")
	public String home(Model model) {
		List<User> userList = userService.getUsers();
		List<Skill> skillList = skillService.getSkills();

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		if (skillList.isEmpty()) {
			String noneS = "ユーザースキルのデータはありません。　";
			model.addAttribute("dataS", noneS);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("username", auth.getName());
		model.addAttribute("userList", userList);
		model.addAttribute("skillList", skillList);
		model.addAttribute("user", new User());
		model.addAttribute("skill", new Skill());

		return "admin";
	}

	@GetMapping("/admin/users/search")
	public String usersSearch(@RequestParam("uKeyword") String uKeyword, Model model) {

		List<User> userList = userService.searchUsers(uKeyword);
		List<Skill> skillList = skillService.getSkills();

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		if (skillList.isEmpty()) {
			String noneS = "ユーザースキルのデータはありません。　";
			model.addAttribute("dataS", noneS);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("username", auth.getName());
		model.addAttribute("userList", userList);
		model.addAttribute("skillList", skillList);
		model.addAttribute("user", new User());
		model.addAttribute("skill", new Skill());
		model.addAttribute("uKeyword", uKeyword);

		return "admin";
	}

	@GetMapping("/admin/skills/search")
	public String skillsSearch(@RequestParam(value = "sKeyword", required = false) String sKeyword,
			@RequestParam(value = "sort", required = false) String sort, Model model) {

		List<User> userList = userService.getUsers();
		List<Skill> skillList = skillService.searchSkills(sKeyword, sort);

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		if (skillList.isEmpty()) {
			String noneS = "ユーザースキルのデータはありません。　";
			model.addAttribute("dataS", noneS);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("username", auth.getName());
		model.addAttribute("userList", userList);
		model.addAttribute("skillList", skillList);
		model.addAttribute("user", new User());
		model.addAttribute("skill", new Skill());
		model.addAttribute("sKeyword", sKeyword);
		model.addAttribute("sort", sort);

		return "admin";
	}

	@PostMapping("/admin/user/insert")
	public String userInsert(@ModelAttribute User user, RedirectAttributes redirectAttributes) {

		String message = userService.insertUser(user);

		redirectAttributes.addFlashAttribute("messageU", message);
		

		return "redirect:/admin/home";

	}
	
	@PostMapping("/admin/skill/insert")
	public String skillInsert(@ModelAttribute Skill skill, RedirectAttributes redirectAttributes) {

		String message = skillService.insertSkill(skill);

		redirectAttributes.addFlashAttribute("messageS", message);

		return "redirect:/admin/home";

	}

	@PostMapping("/admin/user/delete")
	public String userDelete(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
		String message = userService.deleteUser(id);

		redirectAttributes.addFlashAttribute("messageU", message);

		return "redirect:/admin/home";
	}

	@PostMapping("/admin/skill/delete")
	public String skillDelete(@RequestParam("id") Integer id,RedirectAttributes redirectAttributes) {

		String message = skillService.deleteSkill(id);

		redirectAttributes.addFlashAttribute("messageS", message);

		return "redirect:/admin/home";

	}

}
