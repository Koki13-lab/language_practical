package com.forgeon.membermanagement.dto;

public class Skill {
	
	private Integer id;
	private Integer userId;
	private String skill; 
	private String name; 

	public Skill() {
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}
	
	public void setSkill(String skill) {
		this.skill = skill;
	}

	public String getSkill() {
		return skill;
	}
	
	public void setName(String name) {
		this. name = name;
	}

	public String getName() {
		return name;
	}

}
