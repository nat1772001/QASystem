package nhom5.QASystem.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nhom5.QASystem.entities.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
