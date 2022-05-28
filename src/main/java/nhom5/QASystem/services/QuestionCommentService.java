package nhom5.QASystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhom5.QASystem.entities.QuestionComment;
import nhom5.QASystem.repositories.QuestionCommentRepository;

@Service
public class QuestionCommentService {
	private QuestionCommentRepository rep;
	@Autowired
	public QuestionCommentService(QuestionCommentRepository rep) {
		this.rep=rep;
	}
	public void save(QuestionComment questionComment) {
		this.rep.save(questionComment);
	}
	public QuestionComment findById(int id) {
		return rep.findById(id).get();
	}
	public void deleteQuestionComment(QuestionComment questionComment) {
		rep.delete(questionComment);
	}
}
