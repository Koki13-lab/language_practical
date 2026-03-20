package com.forgeon.membermanagement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.postgresql.util.PSQLException;

import com.forgeon.membermanagement.dto.User;

@Mapper
public interface UserMapper {
	User findByName(String name);
	
	List<User> getUsers();
	
	List<User> searchUsers(String uKeyword);
	
	void insertUser(User user)throws PSQLException;
	
	void deleteUser(Integer id)throws PSQLException;
	
}
