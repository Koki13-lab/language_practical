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

import com.forgeon.todo.dto.User;
import com.forgeon.todo.security.CustomUserDetails;
import com.forgeon.todo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("user/list")
	public String userList(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		String loginName = loginUser.getUser().getName();

		List<User> userList = userService.getUsers();

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		model.addAttribute("userList", userList);
		model.addAttribute("loginName", loginName);

		return "user/userlist";
	}

	@GetMapping("user/list/search")
	public String userSearch(@RequestParam("name") String name, @AuthenticationPrincipal CustomUserDetails loginUser,
			Model model) {

		String loginName = loginUser.getUser().getName();

		List<User> userList = userService.searchUsers(name);

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		model.addAttribute("userList", userList);
		model.addAttribute("loginName", loginName);

		return "user/userlist";

	}

	@GetMapping("user/infor")
	public String userInfor(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			Model model) {

		String loginName = loginUser.getUser().getName();

		User userInfor = userService.getUserInfor(id);

		model.addAttribute("user", new User());
		model.addAttribute("userInfor", userInfor);
		model.addAttribute("loginName", loginName);

		return "user/userinfor";
	}

	@GetMapping("/user/infor/update")
	public String userInforUpdate(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		User userInfor = userService.getUserInfor(id);
		User user = userService.getUserInfor(id);

		if (!userService.existsUser(id, loginUser.getId(), loginUser.getUser().getRole())) {

			redirectAttributes.addFlashAttribute("messageU", "他ユーザーの情報は編集できません。");

			return "redirect:/user/infor?id=" + id;
		}

		String loginName = loginUser.getUser().getName();

		model.addAttribute("user", user);
		model.addAttribute("userInfor", userInfor);
		model.addAttribute("isSelf", id.equals(loginUser.getUser().getId()));
		model.addAttribute("loginName", loginName);

		return "user/userinforupdate";
	}

	@PostMapping("/user/infor/update/Complete")
	public String userInforUpdateComplete(@Valid @ModelAttribute User user, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails loginUser, RedirectAttributes redirectAttributes, Model model) {

		String loginName = loginUser.getUser().getName();

		if (result.hasErrors()) {

			User userInfor = userService.getUserInfor(user.getId());

			model.addAttribute("user", user);
			model.addAttribute("userInfor", userInfor);
			model.addAttribute("isSelf", user.getId().equals(loginUser.getUser().getId()));
			model.addAttribute("loginName", loginName);

			return "user/userinforupdate";
		}

		user.setUpdatedAt(LocalDateTime.now());
		user.setUpdatedBy(loginUser.getId());

		String message = userService.updateUser(user, loginUser);

		if ("更新に成功しました。".equals(message)) {
			redirectAttributes.addFlashAttribute("messageU", message);
			return "redirect:/user/list";
		}

		User userInfor = userService.getUserInfor(user.getId());

		model.addAttribute("user", user);
		model.addAttribute("userInfor", userInfor);
		model.addAttribute("loginName", loginName);

		return "user/userinforupdate";

	}

	@GetMapping("/user/infor/delete")
	public String userInforDelete(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		if (!userService.existsUser(id, loginUser.getId(), loginUser.getUser().getRole())) {

			redirectAttributes.addFlashAttribute("messageU", "他ユーザーの情報は編集できません。");

			return "redirect:/user/infor?id=" + id;
		}

		String loginName = loginUser.getUser().getName();
		User userInfor = userService.getUserInfor(id);

		model.addAttribute("userInfor", userInfor);
		model.addAttribute("loginName", loginName);

		return "user/userinfordelete";

	}

	@PostMapping("/user/infor/delete")
	public String userInforDeleteComplete(@RequestParam("id") Integer id,
			@AuthenticationPrincipal CustomUserDetails loginUser, HttpServletRequest request, Model model) {

		String loginName = loginUser.getUser().getName();

		User user = new User();

		user.setId(id);
		user.setDeletedAt(LocalDateTime.now());
		user.setDeletedBy(loginUser.getId());
		user.setDeleted(true);

		userService.deleteUser(user);

		if (id.equals(loginUser.getId())) {
			request.getSession().invalidate();
			return "redirect:/logout-complete";
		}

		model.addAttribute("loginName", loginName);

		return "user/userinfordeleteconmplete";

	}

	@GetMapping("user/add")
	public String userAdd(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		String loginName = loginUser.getUser().getName();

		model.addAttribute("isAdmin", "ROLE_ADMIN".equals(loginUser.getUser().getRole()));
		model.addAttribute("user", new User());
		model.addAttribute("loginName", loginName);

		return "user/userAdd";

	}

	@PostMapping("/user/add")
	public String userSubmit(@Valid @ModelAttribute User user, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails loginUser, RedirectAttributes redirectAttributes, Model model) {

		if (user.getPassword() == null || user.getPassword().isBlank()) {
			result.rejectValue("password", null, "パスワードを入力してください");
		}

		if ("ROLE_ADMIN".equals(user.getRole()) && !"ROLE_ADMIN".equals(loginUser.getUser().getRole())) {
			result.rejectValue("role", null, "管理者のみ管理者を登録できます。");
		}

		if (result.hasErrors()) {

			String loginName = loginUser.getUser().getName();

			model.addAttribute("loginName", loginName);

			return "user/userAdd";
		}

		user.setCreatedBy(loginUser.getId());

		String message = userService.insertUser(user);
		redirectAttributes.addFlashAttribute("messageU", message);

		return "redirect:/user/list";

	}

}
