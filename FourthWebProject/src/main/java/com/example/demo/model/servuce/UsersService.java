package com.example.demo.model.servuce;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.dao.UsersDao;
import com.example.demo.model.dto.SkillsDto;
import com.example.demo.model.dto.UsersDto;

@Service
public class UsersService {

	@Autowired
	private UsersDao usersDao;

	public List<UsersDto> getUsers() throws SQLException {
		return usersDao.getUsers();
	}

	public List<SkillsDto> getSkills() throws SQLException {
		return usersDao.getSkills();
	}

	public List<UsersDto> searchUsers(String uKeyword) throws SQLException {
		return usersDao.searchUsers(uKeyword);
	}

	public List<SkillsDto> searchSkills(String sKeyword, String sort) throws SQLException {
		return usersDao.searchSkills(sKeyword, sort);
	}

	public void insertUser(String userName) throws SQLException {
		usersDao.insertUser(userName);
	}
	
	public void insertSkill(BigDecimal userId,String userSkill) throws SQLException {
		usersDao.insertSkill(userId,userSkill);
	}

	public void deleteUser(int id) throws SQLException {
		usersDao.deleteUser(id);
	}
	
	public void deleteSkill(int id) throws SQLException {
		usersDao.deleteSkill(id);
	}
	
}