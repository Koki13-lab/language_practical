package com.forgeon.todo.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.forgeon.todo.dto.Todo;
import com.forgeon.todo.dto.User;
import com.forgeon.todo.dto.UserTodo;
import com.forgeon.todo.security.CustomUserDetails;
import com.forgeon.todo.service.TodoService;
import com.forgeon.todo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class TodoController {

	@Autowired
	TodoService todoService;

	@Autowired
	UserService userService;

	@GetMapping("todo/list")
	public String todoList(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		String loginName = loginUser.getUser().getName();

		List<Todo> todoList = todoService.getTodo();

		if (todoList.isEmpty()) {
			String noneT = "ユーザーのデータはありません。　";
			model.addAttribute("dataT", noneT);
		}

		model.addAttribute("categoryList", todoService.getCategory());
		model.addAttribute("todo", new Todo());
		model.addAttribute("todoList", todoList);
		model.addAttribute("loginName", loginName);

		return "todo/todolist";
	}

	@GetMapping("/todo/list/search")
	public String todoListSearch(@AuthenticationPrincipal CustomUserDetails loginUser, @ModelAttribute Todo todo,
			@RequestParam(name = "from", required = false) String from,Model model) {

		String loginName = loginUser.getUser().getName();

		List<Todo> todoList = todoService.searchTodo(todo);
		
		model.addAttribute("from", from);
		model.addAttribute("todoList", todoList);
		model.addAttribute("categoryList", todoService.getCategory());
		model.addAttribute("loginName", loginName);
		model.addAttribute("todo", todo);

		return "todo/todolist";

	}

	@GetMapping("todo/add")
	public String todoAdd(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		String loginName = loginUser.getUser().getName();

		model.addAttribute("categoryList", todoService.getCategory());
		model.addAttribute("todo", new Todo());
		model.addAttribute("loginName", loginName);

		return "todo/todoadd";

	}

	@PostMapping("/todo/add")
	public String todoSubmit(@Valid @ModelAttribute Todo todo, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails loginUser, RedirectAttributes redirectAttributes, Model model) {

		if (result.hasErrors()) {

			String loginName = loginUser.getUser().getName();

			model.addAttribute("categoryList", todoService.getCategory());
			model.addAttribute("loginName", loginName);

			return "todo/todoadd";
		}
		todo.setCreatedBy(loginUser.getId());

		String message = todoService.insertTodo(todo);

		redirectAttributes.addFlashAttribute("messageT", message);

		return "redirect:/todo/list";

	}

	@GetMapping("/todo/infor")
	public String todoSubmit(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			Model model) {

		String loginName = loginUser.getUser().getName();

		Todo todoInfor = todoService.pickTodo(id);

		List<User> userList = userService.getUsers();

		if (userList.isEmpty()) {
			String noneT = "ユーザーのデータはありません。　";
			model.addAttribute("dataT", noneT);
		}

		List<UserTodo> userTodoList = todoService.getUserTodo(id);

		model.addAttribute("userTodoList", userTodoList);

		model.addAttribute("userList", userList);
		model.addAttribute("todoInfor", todoInfor);
		model.addAttribute("loginName", loginName);
		model.addAttribute("userTodo", new UserTodo());
		model.addAttribute("user", new User());

		return "todo/todoInfor";

	}

	@GetMapping("/todo/infor/update")
	public String todoInforUpdate(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		if (!todoService.existsUserTodo(id, loginUser.getId(), loginUser.getUser().getRole())) {

			redirectAttributes.addFlashAttribute("messageT", "自分が所属していないTODOは編集できません。");

			return "redirect:/todo/infor?id=" + id;

		}

		String loginName = loginUser.getUser().getName();

		Todo todo = todoService.pickTodo(id);

		model.addAttribute("loginName", loginName);
		model.addAttribute("todo", todo);
		model.addAttribute("categoryList", todoService.getCategory());
		return "todo/todoinforupdate";

	}

	@PostMapping("/todo/infor/update")
	public String todoInforUpdateComplete(@Valid @ModelAttribute Todo todo, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails loginUser, RedirectAttributes redirectAttributes, Model model) {

		String loginName = loginUser.getUser().getName();

		if (result.hasErrors()) {

			model.addAttribute("loginName", loginName);
			model.addAttribute("categoryList", todoService.getCategory());

			return "todo/todoinforupdate";
		}

		todo.setUpdatedAt(LocalDateTime.now());
		todo.setUpdatedBy(loginUser.getId());

		String message = todoService.updateTodo(todo);

		if ("更新に成功しました。".equals(message)) {
			redirectAttributes.addFlashAttribute("messageT", message);
			return "redirect:/todo/list";
		}

		Todo todoinfor = todoService.pickTodo(todo.getId());

		model.addAttribute("loginName", loginName);
		model.addAttribute("todo", todoinfor);
		model.addAttribute("categoryList", todoService.getCategory());

		return "todo/todoinforupdate";
	}

	@GetMapping("/todo/infor/delete")
	public String todoInforDelete(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		if (!todoService.existsUserTodo(id, loginUser.getId(), loginUser.getUser().getRole())) {

			redirectAttributes.addFlashAttribute("messageU", "自分が所属していないTODOは編集できません。");

			return "redirect:/todo/infor?id=" + id;

		}

		String loginName = loginUser.getUser().getName();

		Todo todoInfor = todoService.pickTodo(id);

		List<UserTodo> userTodoList = todoService.getUserTodo(id);

		model.addAttribute("todoInfor", todoInfor);
		model.addAttribute("userTodoList", userTodoList);
		model.addAttribute("loginName", loginName);

		return "todo/todoinfordelete";
	}

	@PostMapping("/todo/infor/delete")
	public String todoInforDeleteComplete(@RequestParam("id") Integer id,
			@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		String loginName = loginUser.getUser().getName();

		Todo todo = new Todo();

		todo.setId(id);
		todo.setDeletedAt(LocalDateTime.now());
		todo.setDeletedBy(loginUser.getId());
		todo.setDeleted(true);

		todoService.deleteTodo(todo);

		model.addAttribute("loginName", loginName);

		return "todo/todoinfordeletecomplete";
	}

	@PostMapping("/todo/infor/user/add")
	public String todoUserAdd(@ModelAttribute UserTodo userTodo, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		userTodo.setCreatedBy(loginUser.getId());

		String message = todoService.insertUserTodo(userTodo);
		
		redirectAttributes.addFlashAttribute("messageT", message);

		return "redirect:/todo/todo/infor?id=" + userTodo.getTodoId();
	}

	@GetMapping("/todo/infor/user/search")
	public String todoUserSearch(@ModelAttribute User user, @AuthenticationPrincipal CustomUserDetails loginUser,
			Model model) {

		String loginName = loginUser.getUser().getName();

		Todo todoInfor = todoService.pickTodo(user.getTodoId());

		List<User> userList = todoService.searchUsers(user.getName());

		if (userList.isEmpty()) {
			String noneT = "ユーザーのデータはありません。　";
			model.addAttribute("dataT", noneT);
		}

		List<UserTodo> userTodoList = todoService.getUserTodo(user.getTodoId());

		model.addAttribute("userTodoList", userTodoList);

		model.addAttribute("userList", userList);
		model.addAttribute("todoInfor", todoInfor);
		model.addAttribute("loginName", loginName);
		model.addAttribute("userTodo", new UserTodo());
		model.addAttribute("user", new User());

		return "todo/todoInfor";
	}

	@PostMapping("/todo/infor/user/delete")
	public String todoINforUserDelete(@ModelAttribute UserTodo userTodo,
			@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		userTodo.setDeletedAt(LocalDateTime.now());
		userTodo.setDeletedBy(loginUser.getId());
		userTodo.setDeleted(true);

		todoService.deleteUserTodo(userTodo);

		return "redirect:/todo/infor?id=" + userTodo.getTodoId();
	}

}
