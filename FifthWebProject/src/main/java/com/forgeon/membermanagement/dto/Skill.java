package com.forgeon.membermanagement.dto;

public class Skill {
	
	private Integer id;
	private Integer user_id;
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
	
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getUser_id() {
		return user_id;
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
