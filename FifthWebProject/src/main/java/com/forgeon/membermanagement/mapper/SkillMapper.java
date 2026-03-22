package com.forgeon.membermanagement.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.postgresql.util.PSQLException;

import com.forgeon.membermanagement.dto.Skill;

@Mapper
public interface SkillMapper {

	List<Skill> getSkills();
	
	List<Skill> getMySkills(Integer id);

	List<Skill> searchSkills(@Param("sKeyword") String sKeyword, @Param("sort") String sort);

	void insertSkill(Skill skil) throws PSQLException;
	
	int deleteSkill(Integer id)throws PSQLException;
	
	int deleteMySkill(@Param("id") Integer id, @Param("loginUserId") Integer loginUserId)throws PSQLException;

}
