package nhom5.QASystem.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import nhom5.QASystem.entities.Field;

@Repository
public interface FieldRepository extends JpaRepository<Field, Integer> {
	@Query("select f from Field f where "
			+ "concat(f.name, f.description)"
			+ "like %?1%")
	public List<Field> findAll(String keyword);
}
