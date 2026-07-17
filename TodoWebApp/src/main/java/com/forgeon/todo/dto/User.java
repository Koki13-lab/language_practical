package com.forgeon.todo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class User {
	
	private Integer id;
	@NotBlank(message = "名前を入力してください")
	@Size(max = 50, message = "名前は50文字以内で入力してください")
	private String name;
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレス形式で入力してください")
	@Size(max = 256, message = "メールアドレスは256文字以内で入力してください")
	private String mail;
	@Size(max = 255, message = "パスワードは255文字以内で入力してください")
	private String password;
	private String role;
	@Size(max = 2000, message = "備考は2000文字以内で入力してください")
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
