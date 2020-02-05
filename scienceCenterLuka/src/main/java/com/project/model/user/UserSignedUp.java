package com.project.model.user;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.project.model.ScienceArea;
import com.project.model.enums.Role;
import com.project.model.user.tx.Membership;
import com.project.model.user.tx.UserTx;

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
public class UserSignedUp  {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
	
	//*****************************

	@Column
	private String firstName;
	
	@Column
	private String lastName;
	
	@Column
	private String email;
	
	//****************************
	
	@Column
	private String city;
	
	@Column
	private String country;
	
	
	@Column
	private boolean wantToReviewe;
	
	@Column
	private boolean activatedAccount;
	
	@OneToMany(mappedBy = "signedUpUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Membership> memberships;

	@OneToMany(mappedBy = "user")
	private Set<UserTx> userTxs;
	
	@Column
	@Enumerated(value = EnumType.STRING)
	private Role role;
	
	//urednik/recezent
	@Column
	private String vocation;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = {
		        CascadeType.MERGE,
		        CascadeType.REFRESH
		    })
	@JoinTable(name="users_science_areas", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId"), inverseJoinColumns = @JoinColumn(name="sc_areaId", referencedColumnName = "scienceAreaId"))
	private Set<ScienceArea> userScienceAreas;
	
//	@Column(name = "username_fk", unique = true, nullable = false, length = 64)
	@Column(name = "username_fk")
	private String userUsername;
	
	@Column
	private String password;
	
	@Column
	private Double longitude;
	
	@Column
	private Double latitude;

}
