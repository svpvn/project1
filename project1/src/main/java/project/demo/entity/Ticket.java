package project.demo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Data;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class) //createdAt
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String clientName;
	private String clientPhone;

	private String content;
	private boolean status;
	
	@CreatedDate // auto gen to date
	@Column(updatable = false)
	private Date createdAt;
	
	@Temporal(TemporalType.DATE)
	private Date processDate;

	@ManyToOne
	private Department department;



}
