package com.forgeon.todo.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.postgresql.util.PSQLException;

import com.forgeon.todo.dto.User;



@Mapper
public interface UserMapper {
	
	User findByName(String name);
	
	List<User> getUsers();
	
	List<User> searchUsers(User user);
	
	User getUserInfo(Integer id);
	
	int updateUser(User user)throws PSQLException;
	
	int insertUser(User user)throws PSQLException;
	
	void deleteUser(User user)throws PSQLException;
	
	boolean existsActiveUser(Integer id);
	
	boolean existsByName(@Param("id") Integer id,@Param("name")String name);
	
	boolean existsByMail(@Param("id")Integer id,@Param("mail")String mail);
	
}
