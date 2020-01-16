package com.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ScienceArea {

	@Column
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long scienceAreaId;
	
	@Column(unique = true)
	private String scienceAreaName;
	
	@Column(unique = true)
	@Length(min = 3, max = 4)
	private String scienceAreaCode;
	
//	@ManyToMany(mappedBy="scienceAreas", fetch=FetchType.LAZY, cascade = {
//            CascadeType.MERGE,
//            CascadeType.REFRESH
//        })
//	private Set<Magazine> magazines;
	
//	@ManyToMany(mappedBy="reviewerScienceAreas", fetch=FetchType.LAZY, cascade = {
//            CascadeType.MERGE,
//            CascadeType.REFRESH
//        })
//	private Set<UserSignedUp> reviewers;
	
	
}
