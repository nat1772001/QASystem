package nhom5.QASystem.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import nhom5.QASystem.entities.User;
import nhom5.QASystem.repositories.UserRepository;

@Service
public class UserService {
	@Autowired 
	private UserRepository repository;
	
	public List<User> listAllUser() {
		return repository.findAll();
	}
	
	public void save(User user) {
		repository.save(user);
	}
	
	public User get(Integer id) {
		return repository.findById(id).get();
	}
	
	public void delete(Integer id) {
		repository.deleteById(id);
	}
	public boolean checkLogin(User user) {
		List<User> list=repository.findAll();
		for(User i:list) {
			if(i.getUsername().compareTo(user.getUsername())==0 && i.getPassword().compareTo(user.getPassword())==0) {
				user.setId(i.getId());
				return true;
			}
		}
		return false;
	}
}
