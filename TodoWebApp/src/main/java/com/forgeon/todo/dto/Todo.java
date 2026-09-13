package com.forgeon.todo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Todo {

	private Integer id;
	@NotBlank(message = "題名を入力してください")
	@Size(max = 50, message = "は50文字以内で入力してください")
	private String title;
	private Integer priority;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDateFrom;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dueDateTo;
	@NotNull(message = "カテゴリを選んでください。")
	private Integer categoryId;
	private String category;
	@Size(max = 2000, message = "内容は2000文字以内で入力してください")
	private String content;
	private LocalDateTime createdAt;
	private Integer createdBy;
	private LocalDateTime updatedAt;
	private Integer updatedBy;
	private LocalDateTime deletedAt;
	private Integer deletedBy;
	private boolean deleted;
	private Integer userId;
	private String name;
}
