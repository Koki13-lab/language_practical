package com.forgeon.todo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.forgeon.todo.dto.Category;

@Mapper
public interface CategoryMapper {
	
	List<Category> getCategory();

}
