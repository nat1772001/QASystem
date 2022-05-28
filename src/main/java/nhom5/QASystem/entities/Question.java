package nhom5.QASystem.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table
@NoArgsConstructor
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String title;
	
	private String content;
	
	private Integer point;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "field_id")
	private Field field;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<Answer> answers = new ArrayList<>();
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	private List<QuestionComment> comments = new ArrayList<>();
	private Date createAt;
	private Date updateAt;
	@PrePersist
	void createdAt() {
		this.createAt = new Date();
		this.updateAt = new Date();
	}
	@PreUpdate
	void updateAt() {
		this.updateAt=new Date();
	}
	public void addAnswer(Answer answer) {
		this.answers.add(answer);
	}
}
