package nhom5.QASystem.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import nhom5.QASystem.entities.Field;
import nhom5.QASystem.entities.Question;
import nhom5.QASystem.entities.User;
import nhom5.QASystem.services.FieldService;
import nhom5.QASystem.services.QuestionService;
import nhom5.QASystem.services.UserService;

@Controller
@RequestMapping("/fields")
public class FieldController {
	private FieldService fieldService;
	private QuestionService questionService;
	private UserService userService;
	@Autowired
	public FieldController(QuestionService questionService, FieldService fieldService, UserService userService) {
		this.questionService=questionService;
		this.fieldService = fieldService;
		this.userService = userService;
	}
	
	@GetMapping
	public String listFields(Model model, @CookieValue(value="userId", defaultValue="-1") int userId, @Param("keyword") String keyword) {
		model.addAttribute("field", new Field());
		model.addAttribute("keyword", keyword);
		model.addAttribute("fields", this.fieldService.getAllFields(keyword));
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
		return "./FieldViews/fields";
	}
	
	@PostMapping(value = "save")
	public String addNewField(@ModelAttribute("field") Field field) {
		fieldService.save(field);
		return "redirect:/fields";
	}
	
	@GetMapping("/{id}") 
	public String getQuestionField(@PathVariable("id") Integer id, Model model, @CookieValue(value="userId", defaultValue="-1") int userId) {
		model.addAttribute("field", fieldService.getFieldById(id));
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
		return "./FieldViews/fieldDetail";
	}
	
	@GetMapping("/edit/{id}") 
	public ModelAndView editForm(@PathVariable("id") Integer id, @CookieValue(value="userId", defaultValue="-1") int userId) {
		ModelAndView mav = new ModelAndView("./FieldViews/editField");
		try {
			if(userId==-1) {
				mav.addObject("user", null);
			}
			else {
				User user = userService.get(userId);
				mav.addObject("user", user);
				Field field = fieldService.getFieldById(id);
				mav.addObject("field", field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}
	
	@GetMapping("/delete/{id}") 
	public String deleteField(@PathVariable("id") Integer id, @CookieValue(value="userId", defaultValue="-1") int userId) {
		User user = userService.get(userId);
		if(userId==-1) {
			return "redirect:/loginProcess";
		}
		else if(user.getPosition().equals("admin")){
			fieldService.delete(id);
			return "redirect:/fields/";
		}
		else {
			return "redirect:/fields";
		}
	}
	
	@PostMapping("/addQuestion")
	public String addQuestion(@RequestParam("idField") int idField, @RequestParam("title") String title, 
			@RequestParam("content") String content, @CookieValue(value="userId", defaultValue="-1") int userId) {
		if(userId==-1) {
			return "redirect:/loginProcess";
		}
		else {
			Question question=new Question();
			Field field=new Field();
			field.setId(idField);
			User user=new User();
			user.setId(userId);
			question.setUser(user);
			question.setPoint(0);
			question.setField(field);
			question.setTitle(title);
			question.setContent(content);
			questionService.saveQuestion(question);
			return "redirect:/fields/"+idField;
		}
	}
	
}
