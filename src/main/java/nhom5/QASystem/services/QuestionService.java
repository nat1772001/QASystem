package nhom5.QASystem.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import nhom5.QASystem.entities.Field;
import nhom5.QASystem.entities.Question;
import nhom5.QASystem.repositories.QuestionRepository;

@Service
public class QuestionService {
	private QuestionRepository rep;
	@Autowired
	public QuestionService(QuestionRepository rep) {
		this.rep=rep;
	}
	public List<Question> getAllQuestions(String keyword){
		if(keyword != null) {
			return rep.findAll(keyword);
		}
		return rep.findAll();
	}
	public Question getQuestionById(int id) {
		return rep.getById(id);
	}
	public void saveQuestion(Question question) {
		rep.save(question);
	}
	public ArrayList<Question> newQuestion(){
		ArrayList<Question> list=rep.findAllByOrderByCreateAtDesc();
		if(list.size()>10) {
			list=(ArrayList<Question>) list.subList(0, 10);
		}
		return list;
	}
	public void delete(Question question) {
		this.rep.delete(question);
	}
	public void deleteById(Integer id) {
		this.rep.deleteById(id);
	}
	
	public ArrayList<Question> findAllByOrderByCreateAtDesc() {
		return rep.findAllByOrderByCreateAtDesc();
	}

	public Question save(Question entity) {
		return rep.save(entity);
	}

	public List<Question> findAll() {
		return rep.findAll();
	}

	public List<Question> findAll(Sort sort) {
		return rep.findAll(sort);
	}

	public List<Question> findAllById(List<Integer> ids) {
		return rep.findAllById(ids);
	}

	public List<Question> saveAll(List<Question> entities) {
		return rep.saveAll(entities);
	}

	public boolean existsById(Integer id) {
		return rep.existsById(id);
	}

	public List<Question> list10Question() {
		return rep.list10Question();
	}

	public void deleteAllById(List<Integer> ids) {
		rep.deleteAllById(ids);
	}

	public ArrayList<Question> listQuestionByTopField() {
		return rep.listQuestionByTopField();
	}

	public ArrayList<Question> listQuestionByTopUser() {
		return rep.listQuestionByTopUser();
	}

	public void deleteAll() {
		rep.deleteAll();
	}
}
