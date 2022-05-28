package nhom5.QASystem.controllers;

import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nhom5.QASystem.dto.UserDTO;

import nhom5.QASystem.entities.Question;
import nhom5.QASystem.entities.User;
import nhom5.QASystem.services.QuestionService;
import nhom5.QASystem.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	private UserService userService;
	private QuestionService questionService;

	@Autowired
	public UserController(UserService userService, QuestionService questionService) {
		this.userService = userService;
		this.questionService = questionService;
	}
// Phần của Độ
	@GetMapping("/profile")
	public String userInfo(Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		User currentUser = userService.get(userId);
		model.addAttribute("user", currentUser);
		return "./UserViews/profile";
	}

	@GetMapping("/edit")
	public String edit(Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		User currentUser = userService.get(userId);
		UserDTO dto = new UserDTO();
		BeanUtils.copyProperties(currentUser, dto);
		dto.setPassword("");// theo nghiệp vụ cần phải xóa trắng password đi sau đó tạo mới
		model.addAttribute("user", dto);
		return "./UserViews/edit-profile";
	}

	@PostMapping("/edit")
	public String update(Model model, UserDTO dto, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		User user = userService.get(userId);
		try {
			if (!StringUtils.isEmpty(dto.getPassword())) {
				user.setUsername(dto.getUsername());
				user.setPassword(dto.getPassword());
				user.setName(dto.getName());
				userService.save(user);
			} else {
				user.setUsername(dto.getUsername());
				user.setName(dto.getName());
				userService.save(user);
			}
			model.addAttribute("message", "update sucessful !");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "redirect:/user/profile";
	}

	@GetMapping("/list")
	// sử dụng optional vì biến message có thể có, có thể không có
	public String list(ModelMap model, @RequestParam(value = "message") Optional<String> message) {
		List<User> list = userService.listAllUser();
		model.addAttribute("list", list);
		if (message.isPresent()) {
			model.addAttribute("message", message.get());// phương thức get dùng để lấy dữ liệu kiểu Optional
		}

		return "./UserViews/listUser";
	}

	@GetMapping("/delete/{id}")
	public ModelAndView deleteUser(ModelMap model, @PathVariable("id") int id,
			@CookieValue(value = "userId", defaultValue = "-1") int userId) {
		try {
			if (id == userId) {
				model.addAttribute("message", "khong the xoa user dang dang nhap !");
			} else {
				userService.delete(id);
				model.addAttribute("message", "xoa tai khoan thanh cong !");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView("redirect:/user/list", model);
	}

//	Của Vinh

	@GetMapping("/listQuestion")
	public String listQuestion(@CookieValue(value = "userId", defaultValue = "-1") int userId, Model model) {
		User user = this.userService.get(userId);
		model.addAttribute("user", user);
		model.addAttribute("questions", user.getQuestions());
		model.addAttribute("userId", userId);
		return "./UserViews/listQuestion";
	}

	@GetMapping("/questions/delete/{id}")
	public String deleteQuestion(@PathVariable("id") int id) {
		Question question = questionService.getQuestionById(id);
		this.questionService.delete(question);
		return "redirect:/user/listQuestion";
	}

	@GetMapping("/questions/update/{id}")
	public String updateQuestion(@PathVariable("id") int id, Model model,
			@CookieValue(value = "userId", defaultValue = "-1") int userId) {
		Question question = questionService.getQuestionById(id);
		model.addAttribute("user", userService.get(userId));
		model.addAttribute("question", question);
		return "./UserViews/updateQuestion";
	}

	@PostMapping("/questions/update")
	public String updateQuestion(Question question) {
		Question temp = questionService.getQuestionById(question.getId());
		temp.setTitle(question.getTitle());
		temp.setContent(question.getContent());
		this.questionService.saveQuestion(temp);
		return "redirect:/user/listQuestion";
	}
}
