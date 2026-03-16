package com.forgeon.membermanagement.model.servuce;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forgeon.membermanagement.model.dao.UsersDao;

import com.forgeon.membermanagement.model.dto.UsersDto;

@Service
public class UsersService {

	@Autowired
	private UsersDao usersDao;

	public List<UsersDto> getUsers() throws SQLException {
		return usersDao.getUsers();
	}

	public List<UsersDto> searchUsers(String uKeyword) throws SQLException {
		return usersDao.searchUsers(uKeyword);
	}

	public void insertUser(String userName) throws SQLException {
		usersDao.insertUser(userName);
	}

	public void deleteUser(int id) throws SQLException {
		usersDao.deleteUser(id);
	}

}