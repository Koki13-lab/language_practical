package com.forgeon.todo.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserTodo {

	private Integer id;
	private String name;
	private Integer userId;
	private Integer todoId;
	private LocalDateTime createdAt;
	private Integer createdBy;
	private LocalDateTime updatedAt;
	private Integer updatedBy;
	private LocalDateTime deletedAt;
	private Integer deletedBy;
	private boolean deleted;

}
