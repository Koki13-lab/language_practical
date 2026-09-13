package com.forgeon.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.forgeon.todo.dto.User;
import com.forgeon.todo.dto.UserTodo;
import com.forgeon.todo.mapper.UserMapper;
import com.forgeon.todo.mapper.UserTodoMapper;
import com.forgeon.todo.security.CustomUserDetails;

@Service
public class UserService {

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	UserTodoMapper userTodoMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getUsers() {
		return userMapper.getUsers();
	}

	public List<User> searchUsers(User user) {
		return userMapper.searchUsers(user);
	}

	public User getUserInfo(Integer id) {
		return userMapper.getUserInfo(id);
	}

	public boolean existsUser(Integer id, Integer loginId, String role) {

		return !("ROLE_TODO".equals(role) && !(id.equals(loginId)));

	}
	
	public boolean existsByName(Integer id,String name) {
	    return userMapper.existsByName(id,name);
	}
	
	public boolean existsByMail(Integer id,String mail) {
	    return userMapper.existsByMail(id,mail);
	}

	public String updateUser(User user, CustomUserDetails loginUser) {
		try {

			String loginRole = loginUser.getUser().getRole();
			User dbUser = userMapper.getUserInfo(user.getId());

			if (!"ROLE_ADMIN".equals(loginRole) && !dbUser.getRole().equals(user.getRole())) {
				return "不正な操作です。";
			}
			if (!"ROLE_ADMIN".equals(loginRole)) {
				user.setRole(dbUser.getRole());
			}

			userMapper.updateUser(user);
			return "更新に成功しました。";
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

	public String deleteUser(User user) {
		try {
			userMapper.deleteUser(user);
			
			UserTodo userTodo = new UserTodo();
			
			userTodo.setDeletedAt(user.getDeletedAt());
			userTodo.setDeletedBy(user.getDeletedBy());
			userTodo.setDeleted(true);
			userTodo.setUserId(user.getId());
			
			userTodoMapper.deleteUserTodoByUserId(userTodo);
			return "削除に成功しました。";
		} catch (org.postgresql.util.PSQLException e) {
			System.out.println(e.getMessage());
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "予期しないDBエラーが発生しました。";
		}
	}

}
