package com.forgeon.membermanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.forgeon.membermanagement.dto.User;
import com.forgeon.membermanagement.mapper.UserMapper;

@Service
public class UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserMapper userMapper;

	public List<User> getUsers() {
		return userMapper.getUsers();
	}

	public List<User> searchUsers(String uKeyword) {
		return userMapper.searchUsers(uKeyword);
	}

	public String insertUser(User user) {
		try {
			String rawPassword = user.getPassword();
			String encodedPassword = passwordEncoder.encode(rawPassword);
			user.setPassword(encodedPassword);
			userMapper.insertUser(user);
			return "登録に成功しました。";
		} catch (org.postgresql.util.PSQLException e) {
			if ("22001".equals(e.getSQLState())) {
				return "文字数オーバーです。";
			} else {
				return "DBエラーが発生しました。";
			}
		} catch (Exception e) {
			return "予期しないエラーが発生しました。";
		}
	}

	public String deleteUser(Integer id) {
		try {
			userMapper.deleteUser(id);
			return "削除に成功しました。";
		} catch (org.postgresql.util.PSQLException e) {
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないDBエラーが発生しました。";
		}
	}
}
