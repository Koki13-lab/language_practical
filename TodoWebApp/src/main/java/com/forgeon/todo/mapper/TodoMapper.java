package com.forgeon.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.postgresql.util.PSQLException;

import com.forgeon.todo.dto.Todo;

@Mapper
public interface TodoMapper {

	List<Todo> getTodo();
	
	List<Todo> searchTodo(Todo todo);
	
	int updateTodo(Todo todo)throws PSQLException;
	
	int insertTodo(Todo todo)throws PSQLException;
	
	Todo pickTodo(Integer id);
	
	void deleteTodo(Todo todo)throws PSQLException;

}
