package com.forgeon.membermanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forgeon.membermanagement.dto.Skill;
import com.forgeon.membermanagement.mapper.SkillMapper;

@Service
public class SkillService {
	
	@Autowired
	SkillMapper skillMapper;
	
	public List<Skill> getSkills(){
		return skillMapper.getSkills();
	}
	
	public List<Skill> getMySkills(Integer id){
		return skillMapper.getMySkills(id);
	}

	public List<Skill> searchSkills(String sKeyword,String sort) {
		return skillMapper.searchSkills(sKeyword,sort);
	}
	
	public String insertSkill(Skill skill) {
		try {
			
			skillMapper.insertSkill(skill);
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
	
	public String deleteSkill(Skill skill) {
		try {
			skillMapper.deleteSkill(skill);
			return "削除に成功しました。";
		}catch (org.postgresql.util.PSQLException e) {
				return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないDBエラーが発生しました。";
		}
	}
}
