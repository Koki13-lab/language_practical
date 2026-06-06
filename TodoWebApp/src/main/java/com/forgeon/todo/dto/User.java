package com.forgeon.todo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class User {
	
	private Integer id;
	@NotBlank(message = "名前を入力してください")
	private String name;
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレス形式で入力してください")
	private String mail;
	@NotBlank(message = "パスワードを入力してください")
	private String password;
	private String role;
	private String remarks;
	private LocalDateTime createdAt;
	private Integer createdBy;
	private LocalDateTime updatedAt;
	private Integer updatedBy;
	private LocalDateTime deletedAt;
	private Integer deletedBy;
	private boolean deleted;
	private Integer todoId;
	

}
