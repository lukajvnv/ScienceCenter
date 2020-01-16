package com.project.model.user.tx;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.project.model.Magazine;
import com.project.model.user.UserSignedUp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class Membership {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long membershipId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserSignedUp signedUpUser;
	
	@ManyToOne
	@JoinColumn(name = "magazine_id")
	private Magazine magazine;
	
	@Column
	private Date startAt;
	
	@Column
	private Date endAt;
	
	@Column
	private Float price;

}
