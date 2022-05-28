package nhom5.QASystem.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nhom5.QASystem.entities.Answer;
import nhom5.QASystem.entities.AnswerComment;
import nhom5.QASystem.entities.Field;
import nhom5.QASystem.entities.Question;
import nhom5.QASystem.entities.QuestionComment;
import nhom5.QASystem.entities.User;
import nhom5.QASystem.services.AnswerCommentService;
import nhom5.QASystem.services.AnswerService;
import nhom5.QASystem.services.QuestionCommentService;
import nhom5.QASystem.services.QuestionService;
import nhom5.QASystem.services.UserService;

@Controller
@RequestMapping("/questions")
public class QuestionController {
	private QuestionService questionService;
	private UserService userService;
	private AnswerService answerService;
	private QuestionCommentService qcService;
	private AnswerCommentService acService;
	
	@Autowired
	public QuestionController(QuestionService questionService, UserService userService,
			AnswerService answerService, QuestionCommentService qcService, AnswerCommentService acService) {
		this.questionService = questionService;
		this.userService = userService;
		this.answerService=answerService;
		this.qcService=qcService;
		this.acService=acService;
	}
// Của Tuấn
	@GetMapping("/result")
	public String searchQuestion(Model model, @CookieValue(value="userId", defaultValue="-1") int userId, @Param("keyword") String keyword) {
		model.addAttribute("keyword", keyword);
		model.addAttribute("questions", this.questionService.getAllQuestions(keyword));
		try {
			if(userId==-1) {
				model.addAttribute("user", null);
			}
			else {
				User user = userService.get(userId);
				model.addAttribute("user", user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "./HomeViews/result";
	}
	
	//Của Độ
	@GetMapping("/list")
	public String viewQuestions(Model model, @RequestParam("field") Optional<String> field,
			@RequestParam("field2") Optional<String> field2, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		// nếu trường field rỗng, thực hiện lấy danh sách bình thường, thay điều kiện
		// sắp xếp là title
		// nếu trường field không rỗng, thực hiện chỉ lấy danh sách 10 đối tượng, viết
		// hàm lấy trong repository
		// lấy ra danh sách 10 đối tượng đã được sắp xếp sẵn, không sử dụng sort nữa,
		// sort vẫn giữ nguyên khi không có điều kiện sắp xếp
		try {
			if(userId==-1) {
				model.addAttribute("user", null);
			}
			else {
				model.addAttribute("user", userService.get(userId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (field2.isPresent()) {
			model.addAttribute("condition", true);
			// title
			if (field2.get().equals("topquestion")) {
				List<Question> ls = questionService.list10Question();
				model.addAttribute("questions", ls);
				model.addAttribute("field2", "topquestion");
			} else if (field2.get().equals("topfield")) {
				List<Question> ls = questionService.listQuestionByTopField();
				model.addAttribute("questions", ls);
				model.addAttribute("field2", "topfield");
			}
			else if (field2.get().equals("topuser")) {
				List<Question> ls = questionService.listQuestionByTopUser();
				model.addAttribute("questions", ls);
				model.addAttribute("field2", "topuser");
			}
		}

		// bên dưới là điều kiện mặc định, trường field có giá trị hoặc không có giá trị
		else {
			Sort sort = Sort.by(Direction.ASC, field.orElse("title"));// nếu trường field rỗng thì mặc định xếp theo
																		// title
			List<Question> ls = questionService.findAll(sort);
			model.addAttribute("questions", ls);
			model.addAttribute("condition", false);
		}
		return "./QuestionViews/questions";
	}
//	Của Vinh
	@GetMapping("/{id}")
	public String detailQuestion(@PathVariable("id") int id, Model model, 
			@CookieValue(value = "userId", defaultValue = "-1") int userId) {
		Question question = questionService.getQuestionById(id);
		User user = null;
		if(userId != -1) {
			user = userService.get(userId); 
		}
		model.addAttribute("question", question);
		model.addAttribute("userId", userId);
		model.addAttribute("user", user);
		return "./QuestionViews/detailQuestion";
	}

	@PostMapping("/answer")
	public String commentQuestion(@RequestParam("idQuestion") int idQuestion, Model model,
			@RequestParam("content") String content,@CookieValue(value = "userId", defaultValue = "-1") int userId) {
		User user;
		if (userId == -1) {
			return "redirect:/loginProcess";
		} else {
			Question question = questionService.getQuestionById(idQuestion);
			Answer answer = new Answer();
			answer.setContent(content);
			answer.setUser(userService.get(userId));
			answer.setQuestion(question);
			answer.setPoint(0);
			question.addAnswer(answer);
			model.addAttribute("userId", userId);
			this.questionService.saveQuestion(question);
			return "redirect:/questions/" + question.getId();
		}
	}

	
//	@GetMapping("/delete/{id}") 
//	public String deleteField(@PathVariable("id") Integer id, @CookieValue(value="userId", defaultValue="-1") int userId) {
//		User user = userService.get(userId);
//		Question question = questionService.getQuestionById(id);
//		if(userId==-1) {
//			return "redirect:/loginProcess";
//		}
//		else if(user.getPosition().equals("admin")){
//			questionService.deleteById(id);
//			return "redirect:/fields/" + question.getField().getId();
//		}
//		else {
//			return "redirect:/fields/" + question.getField().getId();
//		}
//	}
//	
	
	@PostMapping("/vote")
	public String voteQuestion(@RequestParam("idQuestion") int idQuestion, Model model,
			@CookieValue(value = "userId", defaultValue = "-1") int userId, HttpServletRequest request,
			HttpServletResponse response) {
		if (userId == -1) {
			return "redirect:/loginProcess";
		} else {
			Question question = questionService.getQuestionById(idQuestion);
			boolean value = checkVote(request, response, idQuestion+"-"+"question"+"-"+userId);
			if (value == true) {
				this.deleteStatusVote(response, idQuestion+"-"+"question"+"-"+userId);
				question.setPoint(question.getPoint() - 1);
				this.questionService.saveQuestion(question);
			} else {
				this.createStatusVote(response, idQuestion+"-"+"question"+"-"+userId);
				question.setPoint(question.getPoint() + 1);
				this.questionService.saveQuestion(question);
			}
			model.addAttribute("userId", userId);
			return "redirect:/questions/" + question.getId();
		}
	}

	@PostMapping("/answer/vote")
	public String voteAnswer(@RequestParam("idQuestion") int idQuestion, @RequestParam("idAnswer") int idAnswer,
			Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId, HttpServletRequest request,
			HttpServletResponse response) {
		if (userId == -1) {
			return "redirect:/loginProcess";
		} else {
			Answer answer = answerService.findById(idAnswer);
			boolean value = checkVote(request, response, idAnswer+"-"+"answer"+"-"+userId);
			if (value == true) {
				this.deleteStatusVote(response, idAnswer+"-"+"answer"+"-"+userId);
				answer.setPoint(answer.getPoint() - 1);
				this.answerService.save(answer);
			} else {
				this.createStatusVote(response, idAnswer+"-"+"answer"+"-"+userId);
				answer.setPoint(answer.getPoint() + 1);
				this.answerService.save(answer);
			}
			model.addAttribute("userId", userId);
			return "redirect:/questions/" + idQuestion;
		}
	}
	
	@PostMapping("/answer/delete")
	public String deleteAnswer(@RequestParam("idAnswer") int idAnswer, @RequestParam("idQuestion") int idQuestion,
			Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		Answer answer=answerService.findById(idAnswer);
		this.answerService.deleteAnswer(answer);
		model.addAttribute("userId", userId);
		return "redirect:/questions/" + idQuestion;
		
	}
	
	@PostMapping("/comment/add")
	public String addCommentQuestion(@RequestParam("content") String content, @RequestParam("idQuestion") int idQuestion,
			Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		if (userId == -1) {
			return "redirect:/loginProcess";
		} else {
			QuestionComment questionComment=new QuestionComment();
			questionComment.setQuestion(this.questionService.getQuestionById(idQuestion));
			questionComment.setContent(content);
			questionComment.setUser(this.userService.get(userId));
			this.qcService.save(questionComment);
			model.addAttribute("userId", userId);
			return "redirect:/questions/" + idQuestion;
		}
	}
	
	@PostMapping("/comment/delete")
	public String deleteCommentQuestion(@RequestParam("idCommentQue") int idCommentQue, Model model,
			@RequestParam("idQuestion") int idQuestion, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		QuestionComment questionComment=this.qcService.findById(idCommentQue);
		this.qcService.deleteQuestionComment(questionComment);
		model.addAttribute("userId", userId);
		return "redirect:/questions/" + idQuestion;
	}
	
	@PostMapping("/answer/comment/add")
	public String addCommentAnswer(@RequestParam("idQuestion") int idQuestion, @RequestParam("idAnswer") int idAnswer, @RequestParam("content") String content,
			Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		if (userId == -1) {
			return "redirect:/loginProcess";
		} else {
			AnswerComment anserComment=new AnswerComment();
			anserComment.setAnswer(this.answerService.findById(idAnswer));
			anserComment.setContent(content);
			anserComment.setUser(this.userService.get(userId));
			this.acService.addAnswerComment(anserComment);
			return "redirect:/questions/" + idQuestion;
		}
	}
	@PostMapping("/answer/comment/delete")
	public String deleteCommentAnswer(@RequestParam("idQuestion") int idQuestion, @RequestParam("idCommentAns") int idCommentAns,
			Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		AnswerComment answerComment=this.acService.findById(idCommentAns);
		this.acService.deleteAnswerComment(answerComment);
		model.addAttribute("userId", userId);
		return "redirect:/questions/" + idQuestion;
	}
	@PostMapping("/answer/update")
	public String updateAnswer(@RequestParam("idQuestion") int idQuestion, @RequestParam("idAnswer") int idAnswer, 
			@RequestParam("content") String content, Model model, @CookieValue(value = "userId", defaultValue = "-1") int userId) {
		Answer answer=this.answerService.findById(idAnswer);
		answer.setContent(content);
		this.answerService.save(answer);
		model.addAttribute("userId", userId);
		return "redirect:/questions/" + idQuestion;
	}
	
	private boolean checkVote(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		boolean value = false;
		for (Cookie cookie : cookies) {
			if (name.compareTo(cookie.getName()) == 0) {
				value = true;
				break;
			}
		}
		return value;
	}

	private void deleteStatusVote(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	private void createStatusVote(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, "");
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
