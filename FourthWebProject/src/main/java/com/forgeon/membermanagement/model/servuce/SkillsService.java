package com.forgeon.membermanagement.model.servuce;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forgeon.membermanagement.model.dao.SkillsDao;
import com.forgeon.membermanagement.model.dto.SkillsDto;

@Service
public class SkillsService {
	
	@Autowired
	private SkillsDao skillsDao;
	
	public List<SkillsDto> getSkills() throws SQLException {
		return skillsDao.getSkills();
	}
	
	public List<SkillsDto> searchSkills(String sKeyword, String sort) throws SQLException {
		return skillsDao.searchSkills(sKeyword, sort);
	}
	
	public void insertSkill(BigDecimal userId,String userSkill) throws SQLException {
		skillsDao.insertSkill(userId,userSkill);
	}
	
	public void deleteSkill(int id) throws SQLException {
		skillsDao.deleteSkill(id);
	}


}
