package nhom5.QASystem.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
	@NotEmpty
	@Length(min = 6)
	private String username;
	@NotEmpty
	private String name;
	@Length(min = 6)
	private String password;
}
