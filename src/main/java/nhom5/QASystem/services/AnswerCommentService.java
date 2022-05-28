package nhom5.QASystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhom5.QASystem.entities.AnswerComment;
import nhom5.QASystem.repositories.AnswerCommentRepository;

@Service
public class AnswerCommentService {
	private AnswerCommentRepository repository;
	@Autowired
	public AnswerCommentService(AnswerCommentRepository repository) {
		this.repository=repository;
	}
	public void addAnswerComment(AnswerComment answerComment) {
		this.repository.save(answerComment);
	}
	public AnswerComment findById(int id) {
		return this.repository.findById(id).get();
	}
	public void deleteAnswerComment(AnswerComment answerComment) {
		this.repository.delete(answerComment);
	}
}
