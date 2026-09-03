package com.forgeon.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.postgresql.util.PSQLException;

import com.forgeon.todo.dto.UserTodo;

@Mapper
public interface UserTodoMapper {
	
	int insertUserTodo(UserTodo userTodo)throws PSQLException;
	
	List<UserTodo> getUserTodo(Integer id);
	
	void deleteUserTodo(UserTodo userTodo);
	
	void deleteUserTodoByUserId(UserTodo userTodo);

	void deleteUserTodoByTodoId(UserTodo userTodo);
	
	Boolean existsUserTodo(@Param("todoId") Integer todoId,@Param("loginId") Integer loginId);
	
	Boolean existsActiveUserTodo(@Param("userId") Integer userId,@Param("todoId") Integer todoId);

}
