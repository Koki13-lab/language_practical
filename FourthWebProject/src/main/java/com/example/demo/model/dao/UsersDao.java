package com.example.demo.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.dto.SkillsDto;
import com.example.demo.model.dto.UsersDto;

@Repository
public class UsersDao {

	public List<UsersDto> getUsers() throws SQLException {

		String sql = "SELECT id, name FROM users";
		List<UsersDto> userList = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement1 = connection.prepareStatement(sql);) {

			try (ResultSet resultSet1 = statement1.executeQuery();) {

				while (resultSet1.next()) {

					BigDecimal id = resultSet1.getBigDecimal("id");
					String name = resultSet1.getString("name");

					UsersDto user = new UsersDto(id, name);

					userList.add(user);
				}
			}
		}
		return userList;
	}

	public List<SkillsDto> getSkills() throws SQLException {

		String sql2 = "SELECT s.id,u.name,s.skill FROM users AS u JOIN skills AS s on u.id = s.user_id";
		List<SkillsDto> skillList = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement2 = connection.prepareStatement(sql2);) {

			try (ResultSet resultSet2 = statement2.executeQuery();) {

				while (resultSet2.next()) {

					BigDecimal id = resultSet2.getBigDecimal("id");
					String name = resultSet2.getString("name");
					String skill = resultSet2.getString("skill");

					SkillsDto skills = new SkillsDto(id, name, skill);

					skillList.add(skills);
				}
			}
		}
		return skillList;
	}

	public List<UsersDto> searchUsers(String uKeyword) throws SQLException {

		String sql = "SELECT id, name FROM users WHERE(? IS NULL OR name ~ ?)";
		List<UsersDto> userList = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement1 = connection.prepareStatement(sql);) {

			statement1.setString(1, uKeyword);
			statement1.setString(2, uKeyword);

			try (ResultSet resultSet1 = statement1.executeQuery();) {

				while (resultSet1.next()) {

					BigDecimal id = resultSet1.getBigDecimal("id");
					String name = resultSet1.getString("name");

					UsersDto user = new UsersDto(id, name);

					userList.add(user);
				}
			}
		}
		return userList;
	}

	public List<SkillsDto> searchSkills(String sKeyword, String sort) throws SQLException {

		String sql2 = "SELECT s.id,u.name,s.skill FROM users AS u JOIN skills AS s on u.id = s.user_id WHERE(? IS NULL OR skill ~ ?)";
		List<SkillsDto> skillList = new ArrayList<>();

		if ("skill".equals(sort)) {
			sql2 += " ORDER BY skill ASC";
		} else if ("name".equals(sort)) {
			sql2 += " ORDER BY name ASC";
		}

		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919"); PreparedStatement statement2 = connection.prepareStatement(sql2);) {

			statement2.setString(1, sKeyword);
			statement2.setString(2, sKeyword);

			try (ResultSet resultSet2 = statement2.executeQuery();) {

				while (resultSet2.next()) {

					BigDecimal id = resultSet2.getBigDecimal("id");
					String name = resultSet2.getString("name");
					String skill = resultSet2.getString("skill");

					SkillsDto skills = new SkillsDto(id, name, skill);

					skillList.add(skills);
				}
			}
		}
		return skillList;
	}

	public void insertUser(String userName) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");) {
			statement.setString(1, userName);
			statement.executeUpdate();
		}
	}

	public void insertSkill(BigDecimal userId, String userSkill) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection
						.prepareStatement("INSERT INTO skills(user_id,skill) VALUES (?,?)");) {
			statement.setBigDecimal(1, userId);
			statement.setString(2, userSkill);

			statement.executeUpdate();
		}
	}

	public void deleteUser(int id) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
	}
	
	public void deleteSkill(int id) throws SQLException {
		try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education",
				"postgres", "yosino1919");
				PreparedStatement statement = connection.prepareStatement("DELETE FROM skills WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();
	}
	}
}