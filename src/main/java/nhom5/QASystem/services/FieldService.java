package nhom5.QASystem.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nhom5.QASystem.entities.Field;
import nhom5.QASystem.repositories.FieldRepository;

@Service
public class FieldService {
	private FieldRepository repo;
	@Autowired
	public FieldService(FieldRepository repo) {
		this.repo=repo;
	}
	public List<Field> getAllFields(String keyword){
		if(keyword != null) {
			return repo.findAll(keyword);
		}
		return repo.findAll();
	}
	
	public void save(Field field) {
		repo.save(field);
	}
	
	public Field getFieldById(int id) {
		return repo.findById(id).get();
	}
	
	public void delete(Integer id) {
		repo.deleteById(id);
	}
}
