package com.example.demo.model.dto;

import java.math.BigDecimal;

public class SkillsDto {

	private BigDecimal id;
	private String skill;
	private BigDecimal userId;
	private String name;

	public SkillsDto() {

	}

	public SkillsDto(BigDecimal id, String name, String skill) {
		this.id = id;
		this.name = name;
		this.skill = skill;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getSkill() {
		return skill;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

	public BigDecimal getUserId() {
		return userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
