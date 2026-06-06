package com.forgeon.todo.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.postgresql.util.PSQLException;

import com.forgeon.todo.dto.User;



@Mapper
public interface UserMapper {
	
	User findByName(String name);
	
	List<User> getUsers();
	
	List<User> searchUsers(String name);
	
	User getUserInfor(Integer id);
	
	int updateUser(User user)throws PSQLException;
	
	int insertUser(User user)throws PSQLException;
	
	void deleteUser(User user)throws PSQLException;
	
	boolean existsActiveUser(Integer id);
	
}
