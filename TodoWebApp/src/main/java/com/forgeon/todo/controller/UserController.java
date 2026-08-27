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
import com.forgeon.todo.security.CustomUserDetails;
import com.forgeon.todo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	UserService userService;

	@GetMapping("user/list")
	public String userList(@AuthenticationPrincipal CustomUserDetails loginUser,Model model) {

		String loginName = loginUser.getUser().getName();

		List<User> userList = userService.getUsers();

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}
		
		model.addAttribute("user", new User());
		model.addAttribute("userList", userList);
		model.addAttribute("loginName", loginName);
		model.addAttribute("hideUserList", true);

		return "user/userlist";
	}

	@GetMapping("user/list/search")
	public String userSearch(@ModelAttribute User user, @AuthenticationPrincipal CustomUserDetails loginUser,
			Model model) {

		String loginName = loginUser.getUser().getName();

		List<User> userList = userService.searchUsers(user);

		if (userList.isEmpty()) {
			String noneU = "ユーザーのデータはありません。　";
			model.addAttribute("dataU", noneU);
		}

		model.addAttribute("userList", userList);
		model.addAttribute("loginName", loginName);
		model.addAttribute("hideUserList", true);

		return "user/userlist";

	}

	@GetMapping("user/info")
	public String userInfo(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			Model model) {

		String loginName = loginUser.getUser().getName();

		User userInfo = userService.getUserInfo(id);

		model.addAttribute("user", new User());
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("loginName", loginName);

		return "user/userinfo";
	}

	@GetMapping("/user/info/update")
	public String userInfoUpdate(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		User userInfo = userService.getUserInfo(id);
		User user = userService.getUserInfo(id);

		if (!userService.existsUser(id, loginUser.getId(), loginUser.getUser().getRole())) {

			redirectAttributes.addFlashAttribute("messageU", "他ユーザーの情報は編集できません。");

			return "redirect:/user/info?id=" + id;
		}

		String loginName = loginUser.getUser().getName();

		model.addAttribute("user", user);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isSelf", id.equals(loginUser.getUser().getId()));
		model.addAttribute("loginName", loginName);

		return "user/userinfoupdate";
	}

	@PostMapping("/user/info/update/Complete")
	public String userInfoUpdateComplete(@Valid @ModelAttribute User user, BindingResult result,
			@AuthenticationPrincipal CustomUserDetails loginUser, RedirectAttributes redirectAttributes, Model model) {

		String loginName = loginUser.getUser().getName();
		
		if (userService.existsByName(user.getId(),user.getName())) {
	        result.rejectValue("name", null, "既に登録されている名前です。");
	    }
		
		if (userService.existsByMail(user.getId(),user.getMail())) {
	        result.rejectValue("mail", null, "既に登録されているメールアドレスです。");
	    }

		if (result.hasErrors()) {

			User userInfo = userService.getUserInfo(user.getId());

			model.addAttribute("user", user);
			model.addAttribute("userInfo", userInfo);
			model.addAttribute("isSelf", user.getId().equals(loginUser.getUser().getId()));
			model.addAttribute("loginName", loginName);

			return "user/userinfoupdate";
		}

		user.setUpdatedAt(LocalDateTime.now());
		user.setUpdatedBy(loginUser.getId());

		String message = userService.updateUser(user, loginUser);

		if ("更新に成功しました。".equals(message)) {
			redirectAttributes.addFlashAttribute("messageU", message);
			return "redirect:/user/list";
		}

		User userInfo = userService.getUserInfo(user.getId());

		model.addAttribute("user", user);
		model.addAttribute("userInfo", userInfo);
		model.addAttribute("isSelf", user.getId().equals(loginUser.getUser().getId()));
		model.addAttribute("loginName", loginName);
		model.addAttribute("messageU",message);

		return "user/userinfoupdate";

	}

	@GetMapping("/user/info/delete")
	public String userInfoDelete(@RequestParam("id") Integer id, @AuthenticationPrincipal CustomUserDetails loginUser,
			RedirectAttributes redirectAttributes, Model model) {

		if (!userService.existsUser(id, loginUser.getId(), loginUser.getUser().getRole())) {

			redirectAttributes.addFlashAttribute("messageU", "他ユーザーの情報は編集できません。");

			return "redirect:/user/info?id=" + id;
		}

		String loginName = loginUser.getUser().getName();
		User userInfo = userService.getUserInfo(id);

		model.addAttribute("userInfo", userInfo);
		model.addAttribute("loginName", loginName);

		return "user/userinfodelete";

	}

	@PostMapping("/user/info/delete")
	public String userInfoDeleteComplete(@RequestParam("id") Integer id,
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

		return "user/userinfodeleteconmplete";

	}

	@GetMapping("user/add")
	public String userAdd(@AuthenticationPrincipal CustomUserDetails loginUser, Model model) {

		String loginName = loginUser.getUser().getName();

		model.addAttribute("isAdmin", "ROLE_ADMIN".equals(loginUser.getUser().getRole()));
		model.addAttribute("user", new User());
		model.addAttribute("loginName", loginName);

		return "user/useradd";

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
		
		if (userService.existsByName(user.getId(),user.getName())) {
	        result.rejectValue("name", null, "既に登録されている名前です。");
	    }
		
		if (userService.existsByMail(user.getId(),user.getMail())) {
	        result.rejectValue("mail", null, "既に登録されているメールアドレスです。");
	    }

		if (result.hasErrors()) {

			String loginName = loginUser.getUser().getName();

			model.addAttribute("loginName", loginName);

			return "user/useradd";
		}

		user.setCreatedBy(loginUser.getId());

		String message = userService.insertUser(user);
		redirectAttributes.addFlashAttribute("messageU", message);

		return "redirect:/user/list";

	}

}
