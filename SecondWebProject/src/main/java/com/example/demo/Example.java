package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.postgresql.util.PSQLException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Example {

	@GetMapping("/api/users")
	public List<Map<String, Object>> getUsers(@RequestParam(required = false, name = "keyword") String keyword)
			throws SQLException {
		List<Map<String, Object>> users = new ArrayList<>();
		String sql = "SELECT id,name FROM users";
		if (keyword != null && !keyword.isEmpty()) {
			sql += " WHERE name ~ ? ";
		}

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement = connection.prepareStatement(sql);) {

			if (keyword != null && !keyword.isEmpty()) {
				statement.setString(1, keyword);
			}

			try (ResultSet resultSet = statement.executeQuery();) {

				while (resultSet.next()) {
					users.add(Map.of("id", resultSet.getInt("id"), "name", resultSet.getString("name")));
				}

				return users;
			}
		}
	}

	@PostMapping("/api/users")
	public ResponseEntity<String> insertUser(@RequestBody Map<String, String> body) throws SQLException {
		String name = body.get("name");

		if (name == null || name.isBlank()) {
			return ResponseEntity.badRequest().body("名前は空文字にはできません");
		}

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");) {
			statement.setString(1, name);
			statement.executeUpdate();
			return ResponseEntity.ok("登録成功しました");
		} catch (org.postgresql.util.PSQLException e) {
			String sqlState = e.getSQLState();
			if ("22001".equals(sqlState)) {
				return ResponseEntity.badRequest().body("文字数オーバーです。");
			} else {
				return ResponseEntity.internalServerError().body("DBエラーです。");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("予期せぬDBエラーです。");
		}
	}

	@GetMapping("/api/skills")
	public List<Map<String, Object>> getSkills(@RequestParam(required = false, name = "keyword") String keyword,
			@RequestParam(required = false, name = "sort") String sort) throws SQLException {
		List<Map<String, Object>> skills = new ArrayList<>();
		String sql = "SELECT s.id,u.name,s.skill FROM users AS u JOIN skills AS s on u.id = s.user_id";
		if (keyword != null && !keyword.isEmpty()) {
			sql += " WHERE skill ~ ? ";
		}

		if ("skill".equals(sort)) {
			sql += " ORDER BY skill ASC";
		} else if ("name".equals(sort)) {
			sql += " ORDER BY name ASC";
		}

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement = connection.prepareStatement(sql);) {

			if (keyword != null && !keyword.isEmpty()) {
				statement.setString(1, keyword);
			}

			try (ResultSet resultSet = statement.executeQuery();) {

				while (resultSet.next()) {
					skills.add(Map.of("id", resultSet.getInt("id"), "name", resultSet.getString("name"), "skill",
							resultSet.getString("skill")));
				}

				return skills;
			}
		}
	}

	@PostMapping("/api/skills")
	public ResponseEntity<String> insertSkills(@RequestBody Map<String, String> body) {

		String userid = body.get("userid");
		String skill = body.get("skill");

		if (userid == null || userid.isBlank()) {
			return ResponseEntity.badRequest().body("ユーザーIDを入力してください。");
		}
		if (skill == null || skill.isBlank()) {
			return ResponseEntity.badRequest().body("スキルを入力してください。");
		}

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO skills(user_id,skill) VALUES (?,?)");) {
			statement.setInt(1, Integer.parseInt(userid));
			statement.setString(2, skill);
			statement.executeUpdate();

			return ResponseEntity.ok("登録に成功しました");

		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().body("ユーザーIDは数値で入力してください");
		} catch (org.postgresql.util.PSQLException e) {
			String sqlState = e.getSQLState();
			if ("23503".equals(sqlState)) {
				return ResponseEntity.badRequest().body("入力されたユーザーIDは存在しません");
			} else if ("22001".equals(sqlState)) {
				return ResponseEntity.badRequest().body("文字数オーバーです。");
			} else {
				return ResponseEntity.internalServerError().body("DBエラーです。");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("予期せぬDBエラーです。");
		}
	}

	@DeleteMapping("/api/users/{id}")
	public ResponseEntity<String> deleteUsers(@PathVariable("id") int id) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();

			return ResponseEntity.ok("削除に成功しました");

		} catch (org.postgresql.util.PSQLException e) {
			String sqlState = e.getSQLState();
			if ("23503".equals(sqlState)) {
				return ResponseEntity.badRequest().body("ユーザースキルを削除してください。");
			} else {
				return ResponseEntity.internalServerError().body("DBエラーです。");
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("予期せぬDBエラーです。");
		}
	}

	@DeleteMapping("/api/skills/{id}")
	public ResponseEntity<String> deleteSkill(@PathVariable("id") int id) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();

			return ResponseEntity.ok("削除に成功しました");

		} catch (PSQLException e) {
			return ResponseEntity.badRequest().body("DBエラーにより削除できませんでした");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("予期せぬDBエラーです。");
		}
	}
}
