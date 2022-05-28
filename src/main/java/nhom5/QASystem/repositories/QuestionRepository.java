package nhom5.QASystem.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nhom5.QASystem.entities.Field;
import nhom5.QASystem.entities.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
	public ArrayList<Question> findAllByOrderByCreateAtDesc();
	
	@Query("select q from Question q where "
			+ "concat(q.title, q.content, q.user.name)"
			+ "like %?1%")
	public List<Question> findAll(String keyword);
	
	@Query(value="select * from (select * from question order by point desc) as a limit 10",nativeQuery = true)
	ArrayList<Question> list10Question();
	@Query(value=" select * from question where field_id in\r\n"
			+ "(select field_id from (select field_id, count(field_id) as x from question group by field_id) as tmp\r\n"
			+ " where tmp.x = (select max(tmp2.row1) from (select field_id, count(field_id) as row1 from question group by field_id) as tmp2))",
			nativeQuery = true)
	ArrayList<Question> listQuestionByTopField();
	@Query(value="select question.id, question.content, question.createAt, question.point, question.title, question.updateAt, question.field_id, question.user_id\r\n"
			+ " from question, user where user.id= question.user_id and user_id in \r\n"
			+ "(select user_id \r\n"
			+ " from (\r\n"
			+ " select * from (select user_id, count(user_id) as x from question group by user_id order by x desc) as tmp limit 3) as tmp2)", nativeQuery=true)
	ArrayList<Question> listQuestionByTopUser();
}
