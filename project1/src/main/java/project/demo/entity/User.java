package project.demo.entity; // data tranform object

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Table(name = "user") //map to table SQL
@Entity  
public class User {
	//id khoa chinh
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne //bat buoc kieu du lieu Entity
	// Many user to one department
	//@JoinColumn(name ="department_id")
	private Department department;
	
	private int age;
	private String name;
	//luu ten file path
	private String avatarURL;

	@Column (unique = true)
	private String username;
	
	private String password;
	
	private String homeAddress;
	
	@Temporal(TemporalType.DATE)
	private Date birthdate;
}
