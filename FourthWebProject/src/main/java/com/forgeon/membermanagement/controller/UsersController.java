package com.forgeon.membermanagement.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.forgeon.membermanagement.model.dto.SkillsDto;
import com.forgeon.membermanagement.model.dto.UsersDto;
import com.forgeon.membermanagement.model.servuce.SkillsService;
import com.forgeon.membermanagement.model.servuce.UsersService;

@Controller
public class UsersController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private SkillsService skillsService;

	@GetMapping("/users")
	public String users(Model model) throws SQLException {

		List<UsersDto> userList = usersService.getUsers();
		List<SkillsDto> skillList = skillsService.getSkills();

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		if (skillList.isEmpty()) {
			String noneS = "ユーザースキルのデータはありません。　";
			model.addAttribute("dataS", noneS);
		}

		model.addAttribute("userList", userList);
		model.addAttribute("skillList", skillList);
		model.addAttribute("skills", new SkillsDto());

		return "users";
	}

	@GetMapping("users/search")
	public String uSearch(@RequestParam(required = false, name = "uKeyword") String uKeyword, Model model)
			throws SQLException {

		List<UsersDto> userList = usersService.searchUsers(uKeyword);
		List<SkillsDto> skillList = skillsService.getSkills();

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		if (skillList.isEmpty()) {
			String noneS = "ユーザースキルのデータはありません。　";
			model.addAttribute("dataS", noneS);
		}

		model.addAttribute("userList", userList);
		model.addAttribute("skillList", skillList);
		model.addAttribute("skills", new SkillsDto());
		model.addAttribute("uKeyword", uKeyword);

		return "users";

	}

	@GetMapping("skills/search")
	public String sSearch(@RequestParam(required = false, name = "sKeyword") String sKeyword,
			@RequestParam(required = false, name = "sort") String sort, Model model) throws SQLException {

		List<UsersDto> userList = usersService.getUsers();
		List<SkillsDto> skillList = skillsService.searchSkills(sKeyword,sort);

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}
		
		if (skillList.isEmpty()) {
			String noneS = "ユーザースキルのデータはありません。　";
			model.addAttribute("dataS", noneS);
		}

		model.addAttribute("userList", userList);
		model.addAttribute("skillList", skillList);
		model.addAttribute("skills", new SkillsDto());
		model.addAttribute("sKeyword", sKeyword);
		model.addAttribute("sort", sort);

		return "users";
	}


	@PostMapping("addUser")
	public String uInsert(@RequestParam("name") String name, RedirectAttributes redirectAttributes)
			throws SQLException {
		String userName = name;

		if (name == null || name.isBlank()) {

			redirectAttributes.addFlashAttribute("messageU", "文字を入力してください。");

			return "redirect:/users";
		}

		try {usersService.insertUser(userName);
			redirectAttributes.addFlashAttribute("messageU", "登録に成功しました");

			return "redirect:/users";
		} catch (org.postgresql.util.PSQLException e) {
			String sqlState = e.getSQLState();
			if ("22001".equals(sqlState)) {
				redirectAttributes.addFlashAttribute("messageU", "文字数オーバーです。");
				return "redirect:/users";
			} else {
				redirectAttributes.addFlashAttribute("messageU", "DBエラーが発生しました。");
				return "redirect:/users";
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageU", "予期しないDBエラーが発生しました。");
			return "redirect:/users";
		}
	}

	@PostMapping("addSkill")
	public String sInsert(@ModelAttribute SkillsDto skills, RedirectAttributes redirectAttributes) throws SQLException {

		BigDecimal userId = skills.getUserId();
		String userSkill = skills.getSkill();

		if (userId == null || userSkill == null || userSkill.isBlank()) {
			redirectAttributes.addFlashAttribute("messageS", "入力されていない箇所があります。");
			return "redirect:/users";
		}

		try {skillsService.insertSkill(userId,userSkill);
			redirectAttributes.addFlashAttribute("messageS", "登録に成功しました");

			return "redirect:/users";
		} catch (NumberFormatException e) {
			redirectAttributes.addFlashAttribute("messageS", "ユーザーIDは数値で入力してください。");
			return "redirect:/users";
		} catch (org.postgresql.util.PSQLException e) {
			String sqlState = e.getSQLState();
			if ("23503".equals(sqlState)) {
				redirectAttributes.addFlashAttribute("messageS", "入力されたユーザーIDは存在しません");
				return "redirect:/users";
			} else if ("22001".equals(sqlState)) {
				redirectAttributes.addFlashAttribute("messageS", "文字数オーバーです。");
				return "redirect:/users";
			} else {
				redirectAttributes.addFlashAttribute("messageS", "DBエラーが発生しました。");
				return "redirect:/users";
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageS", "予期しないDBエラーが発生しました。");
			return "redirect:/users";
		}
	}

	@PostMapping("deleteUser")
	public String uDelete(@RequestParam("id") int id, RedirectAttributes redirectAttributes) throws SQLException {
		
			try{usersService.deleteUser(id);
				redirectAttributes.addFlashAttribute("messageU", "削除に成功しました。");

			return "redirect:/users";
		} catch (org.postgresql.util.PSQLException e) {
			String sqlState = e.getSQLState();
			if ("23503".equals(sqlState)) {
				redirectAttributes.addFlashAttribute("messageU", "指定ユーザーのスキルを削除してください。");
				return "redirect:/users";
			} else {
				redirectAttributes.addFlashAttribute("messageU", "DBエラーが発生しました。");
				return "redirect:/users";
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageU", "予期しないDBエラーが発生しました。");
			return "redirect:/users";
		}
	}

	@PostMapping("deleteSkill")
	public String sDelete(@RequestParam("id") int id, RedirectAttributes redirectAttributes) throws SQLException {
		
			try{skillsService.deleteSkill(id);
				redirectAttributes.addFlashAttribute("messageS", "削除に成功しました。");

			return "redirect:/users";
		} catch (PSQLException e) {
			redirectAttributes.addFlashAttribute("messageS", "DBエラーが発生しました。");
			return "redirect:/users";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageS", "予期しないDBエラーが発生しました。");
			return "redirect:/users";
		}
	}

}