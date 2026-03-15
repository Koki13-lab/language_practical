package com.forgeon.membermanagement.model.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.forgeon.membermanagement.model.dto.SkillsDto;
import com.forgeon.membermanagement.model.dto.UsersDto;

@Repository
public class UsersDao {

	private Connection getConnection() throws SQLException {

		return DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql_education", "postgres", "yosino1919");

	}

	public List<UsersDto> getUsers() throws SQLException {

		String sql = "SELECT id, name FROM users";
		List<UsersDto> userList = new ArrayList<>();

		try (Connection connection = getConnection();
				PreparedStatement statement1 = connection.prepareStatement(sql);) {

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

	public List<UsersDto> searchUsers(String uKeyword) throws SQLException {

		String sql = "SELECT id, name FROM users WHERE(? IS NULL OR name ~ ?)";
		List<UsersDto> userList = new ArrayList<>();

		try (Connection connection = getConnection();
				PreparedStatement statement1 = connection.prepareStatement(sql);) {

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

	public void insertUser(String userName) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name) VALUES (?)");) {
			statement.setString(1, userName);
			statement.executeUpdate();
		}
	}

	public void deleteUser(int id) throws SQLException {
		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");) {
			statement.setInt(1, id);
			statement.executeUpdate();
		}
	}
}