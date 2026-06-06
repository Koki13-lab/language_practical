package com.forgeon.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.forgeon.todo.dto.Category;
import com.forgeon.todo.dto.Todo;
import com.forgeon.todo.dto.User;
import com.forgeon.todo.dto.UserTodo;
import com.forgeon.todo.mapper.CategoryMapper;
import com.forgeon.todo.mapper.TodoMapper;
import com.forgeon.todo.mapper.UserMapper;
import com.forgeon.todo.mapper.UserTodoMapper;

@Service
public class TodoService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	TodoMapper todoMapper;

	@Autowired
	CategoryMapper categoryMapper;

	@Autowired
	UserTodoMapper userTodoMapper;

	public List<Todo> getTodo() {

		return todoMapper.getTodo();

	}

	public List<Todo> searchTodo(Todo todo) {

		return todoMapper.searchTodo(todo);

	}

	public List<User> searchUsers(String name) {

		return userMapper.searchUsers(name);

	}

	public Todo pickTodo(Integer id) {

		return todoMapper.pickTodo(id);

	}

	public List<Category> getCategory() {

		return categoryMapper.getCategory();

	}

	public boolean existsUserTodo(Integer id, Integer loginId, String role) {

		return !("ROLE_USER".equals(role) && !userTodoMapper.existsUserTodo(id, loginId));

	}

	public String updateTodo(Todo todo) {
		try {
			todoMapper.updateTodo(todo);
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

	public String insertTodo(Todo todo) {
		try {
			todoMapper.insertTodo(todo);
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

	public String insertUserTodo(UserTodo userTodo) {

		if (!userMapper.existsActiveUser(userTodo.getUserId())) {
			return "存在するユーザーIDを指定してください。";
		}
		
		if (userTodoMapper.existsActiveUserTodo(userTodo.getUserId(),userTodo.getTodoId())) {
			return "既に登録されています。";
		}

		try {
			userTodoMapper.insertUserTodo(userTodo);
			return "登録に成功しました。";
		} catch (org.postgresql.util.PSQLException e) {
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			e.printStackTrace();
			return "予期しないエラーが発生しました。";
		}
	}

	public List<UserTodo> getUserTodo(Integer id) {

		return userTodoMapper.getUserTodo(id);

	}

	public String deleteTodo(Todo todo) {
		try {
			todoMapper.deleteTodo(todo);

			UserTodo userTodo = new UserTodo();

			userTodo.setDeletedAt(todo.getDeletedAt());
			userTodo.setDeletedBy(todo.getDeletedBy());
			userTodo.setDeleted(true);
			userTodo.setTodoId(todo.getId());

			userTodoMapper.deleteUserTodoByTodoId(userTodo);
			return "削除に成功しました。";
		} catch (DataAccessException e) {
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないDBエラーが発生しました。";
		}
	}

	public String deleteUserTodo(UserTodo userTodo) {
		try {
			userTodoMapper.deleteUserTodo(userTodo);
			return "削除に成功しました。";
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないDBエラーが発生しました。";
		}

	}

}
