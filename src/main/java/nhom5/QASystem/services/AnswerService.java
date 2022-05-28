package nhom5.QASystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhom5.QASystem.entities.Answer;
import nhom5.QASystem.repositories.AnswerRepository;

@Service
public class AnswerService {
	private AnswerRepository rep;
	@Autowired
	public AnswerService(AnswerRepository rep) {
		this.rep=rep;
	}
	public Answer findById(int id) {
		return rep.findById(id).get();
	}
	public void deleteAnswer(Answer answer) {
		rep.delete(answer);
	}
	public void save(Answer answer) {
		rep.save(answer);
	}
}
