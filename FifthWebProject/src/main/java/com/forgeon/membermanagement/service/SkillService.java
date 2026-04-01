package com.forgeon.membermanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.forgeon.membermanagement.dto.Skill;
import com.forgeon.membermanagement.mapper.SkillMapper;
import com.forgeon.membermanagement.security.CustomUserDetails;

@Service
public class SkillService {

	@Autowired
	SkillMapper skillMapper;

	private Integer geUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
		return user.getId();
	}

	public List<Skill> getSkills() {
		return skillMapper.getSkills();
	}

	public List<Skill> getMySkills(Integer id) {
		return skillMapper.getMySkills(id);
	}

	public List<Skill> searchSkills(String sKeyword, String sort) {
		return skillMapper.searchSkills(sKeyword, sort);
	}

	public String insertSkill(Skill skill) {
		try {

			skillMapper.insertSkill(skill);
			return "登録に成功しました。";
		} catch (DataIntegrityViolationException e) {
			if (e.getCause() instanceof org.postgresql.util.PSQLException psqlEx) {
				String sqlState = psqlEx.getSQLState();
				if ("22001".equals(sqlState)) {
					return "文字数オーバーです。";
				} else if ("23503".equals(sqlState)) {
					return "入力されたユーザーIDは存在しません";
				}
			}
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないエラーが発生しました。";
		}
	}

	public String deleteSkill(Integer id) {
		try {

			skillMapper.deleteSkill(id);

			return "削除に成功しました。";
		} catch (org.postgresql.util.PSQLException e) {
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないDBエラーが発生しました。";
		}
	}

	public String deleteMySkill(Integer id) {
		try {

			Integer loginUserId = geUserId();

			int count = skillMapper.deleteMySkill(id, loginUserId);

			if (count == 0) {
				return "不正な操作です";
			}

			return "削除に成功しました。";
		} catch (org.postgresql.util.PSQLException e) {
			return "DBエラーが発生しました。";
		} catch (Exception e) {
			return "予期しないDBエラーが発生しました。";
		}
	}
}
