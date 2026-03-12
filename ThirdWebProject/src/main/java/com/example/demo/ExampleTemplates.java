package com.example.demo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExampleTemplates {

	@GetMapping("/users")
	public String users(Model model) throws SQLException {

		String sql = "SELECT id, name FROM users";

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement1 = connection.prepareStatement(sql);) {
			
			try (ResultSet resultSet1 = statement1.executeQuery();) {

				List<Users> userList = new ArrayList<>();
				
				while (resultSet1.next()) {

					BigDecimal id = resultSet1.getBigDecimal("id");
					String name = resultSet1.getString("name");

					Users user = new Users(id, name);

					userList.add(user);
				}
				
				if(userList.isEmpty()) {
					String noneU = "ユーザーのデータはありません。　";
					model.addAttribute("dataU",noneU);
				}

				String sql2 = "SELECT s.id,u.name,s.skill FROM users AS u JOIN skills AS s on u.id = s.user_id";

				try (PreparedStatement statement2 = connection.prepareStatement(sql2);) {

					try (ResultSet resultSet2 = statement2.executeQuery();) {

						List<Skills> skillList = new ArrayList<>();
						
						while (resultSet2.next()) {

							BigDecimal id = resultSet2.getBigDecimal("id");
							String name = resultSet2.getString("name");
							String skill = resultSet2.getString("skill");

							Skills skills = new Skills(id, name, skill);

							skillList.add(skills);
						}
						
						if(skillList.isEmpty()) {
							String noneS = "ユーザースキルのデータはありません。　";
							model.addAttribute("dataS",noneS);
						}

						model.addAttribute("userList", userList);
						model.addAttribute("skillList", skillList);
						model.addAttribute("skills", new Skills());
						

						return "users";
					}
				}
			}
		}
	}
	
	@GetMapping("users/search")
	public String uSearch(@RequestParam(required = false, name = "uKeyword") String uKeyword,Model model) throws SQLException{
		String sql = "SELECT id, name FROM users WHERE(? IS NULL OR name ~ ?)";

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement1 = connection.prepareStatement(sql);) {
			
				statement1.setString(1, uKeyword);
				statement1.setString(2, uKeyword);
				
			try (ResultSet resultSet1 = statement1.executeQuery();) {

				List<Users> userList = new ArrayList<>();
				
				while (resultSet1.next()) {

					BigDecimal id = resultSet1.getBigDecimal("id");
					String name = resultSet1.getString("name");

					Users user = new Users(id, name);

					userList.add(user);
				}
				
				if(userList.isEmpty()) {
					String noneU = "ユーザーのデータはありません。　";
					model.addAttribute("dataU",noneU);
				}

				String sql2 = "SELECT s.id,u.name,s.skill FROM users AS u JOIN skills AS s on u.id = s.user_id";

				try (PreparedStatement statement2 = connection.prepareStatement(sql2);) {

					try (ResultSet resultSet2 = statement2.executeQuery();) {

						List<Skills> skillList = new ArrayList<>();
						
						while (resultSet2.next()) {

							BigDecimal id = resultSet2.getBigDecimal("id");
							String name = resultSet2.getString("name");
							String skill = resultSet2.getString("skill");

							Skills skills = new Skills(id, name, skill);

							skillList.add(skills);
						}
						
						if(skillList.isEmpty()) {
							String noneS = "ユーザースキルのデータはありません。　";
							model.addAttribute("dataS",noneS);
						}

						model.addAttribute("userList", userList);
						model.addAttribute("skillList", skillList);
						model.addAttribute("skills", new Skills());
						model.addAttribute("uKeyword", uKeyword);
						

						return "users";
					}
				}
			}
		}
	}
	
	@GetMapping("skills/search")
	public String sSearch(@RequestParam(required = false, name = "sKeyword") String sKeyword,
			@RequestParam(required = false, name = "sort") String sort,Model model ) throws SQLException{


		String sql = "SELECT id, name FROM users";

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement1 = connection.prepareStatement(sql);) {
			
			try (ResultSet resultSet1 = statement1.executeQuery();) {

				List<Users> userList = new ArrayList<>();
				
				while (resultSet1.next()) {

					BigDecimal id = resultSet1.getBigDecimal("id");
					String name = resultSet1.getString("name");

					Users user = new Users(id, name);

					userList.add(user);
				}
				
				if(userList.isEmpty()) {
					String noneU = "ユーザーのデータはありません。　";
					model.addAttribute("dataU",noneU);
				}

				String sql2 = "SELECT s.id,u.name,s.skill FROM users AS u JOIN skills AS s on u.id = s.user_id WHERE(? IS NULL OR skill ~ ?)";

				if ("skill".equals(sort)) {
					sql2 += " ORDER BY skill ASC";
				} else if ("name".equals(sort)) {
					sql2 += " ORDER BY name ASC";
				}

				try (PreparedStatement statement2 = connection.prepareStatement(sql2);) {

					statement2.setString(1, sKeyword);
					statement2.setString(2, sKeyword);

					try (ResultSet resultSet2 = statement2.executeQuery();) {

						List<Skills> skillList = new ArrayList<>();
						
						while (resultSet2.next()) {

							BigDecimal id = resultSet2.getBigDecimal("id");
							String name = resultSet2.getString("name");
							String skill = resultSet2.getString("skill");

							Skills skills = new Skills(id, name, skill);

							skillList.add(skills);
						}
						
						if(skillList.isEmpty()) {
							String noneS = "ユーザースキルのデータはありません。　";
							model.addAttribute("dataS",noneS);
						}

						model.addAttribute("userList", userList);
						model.addAttribute("skillList", skillList);
						model.addAttribute("skills", new Skills());
						model.addAttribute("sKeyword", sKeyword);
						model.addAttribute("sort", sort);
						

						return "users";
					}
				}
			}
		}
	}

	@PostMapping("addUser")
	public String uInsert(@RequestParam("name") String name,RedirectAttributes redirectAttributes) throws SQLException {
		String userName = name;
		
		if (name == null || name.isBlank()) {
			
			redirectAttributes.addFlashAttribute("messageU", "文字を入力してください。");
			
			return "redirect:/users";
		}

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");) {
			statement.setString(1, userName);
			statement.executeUpdate();
			
			redirectAttributes.addFlashAttribute("messageU", "登録に成功しました");

			return "redirect:/users";
		}catch (org.postgresql.util.PSQLException e) {
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
	public String sInsert(@ModelAttribute Skills skills,RedirectAttributes redirectAttributes) throws SQLException {

		BigDecimal userId = skills.getUserId();
		String userSkill = skills.getSkill();
		
		if (userId == null || userSkill == null || userSkill.isBlank()) {
			redirectAttributes.addFlashAttribute("messageS", "入力されていない箇所があります。");
			return "redirect:/users";
		}
		
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO skills(user_id,skill) VALUES (?,?)");) {
			statement.setBigDecimal(1, userId);
			statement.setString(2, userSkill);

			statement.executeUpdate();
			
			redirectAttributes.addFlashAttribute("messageS", "登録に成功しました");

			return "redirect:/users";
		}catch (NumberFormatException e) {
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
	public String uDelete(@RequestParam("id") int id,RedirectAttributes redirectAttributes) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();
			redirectAttributes.addFlashAttribute("messageU", "削除に成功しました。");

			return "redirect:/users";
		}catch (org.postgresql.util.PSQLException e) {
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
	public String sDelete(@RequestParam("id") int id,RedirectAttributes redirectAttributes) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();
			redirectAttributes.addFlashAttribute("messageS", "削除に成功しました。");

			return "redirect:/users";
		}catch (PSQLException e) {
			redirectAttributes.addFlashAttribute("messageS", "DBエラーが発生しました。");
			return "redirect:/users";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("messageS", "予期しないDBエラーが発生しました。");
			return "redirect:/users";
		}
	}

}