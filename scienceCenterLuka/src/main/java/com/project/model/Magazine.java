package com.project.model;

import java.util.HashSet;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.project.model.enums.WayOfPayment;
import com.project.model.user.UserSignedUp;
import com.project.model.user.tx.Membership;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class Magazine {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long magazineId;
	
	@Column
	private String ISSN;
	
	@Column
	private String name;
	
	@Column
	@ManyToMany(fetch = FetchType.EAGER,cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        })
	@JoinTable(name="magazine_scArea", joinColumns = @JoinColumn(name = "magazine_id", referencedColumnName = "magazineId"), inverseJoinColumns = @JoinColumn(name="sc_areaId", referencedColumnName = "scienceAreaId"))
	private Set<ScienceArea> scienceAreas;
	
	@Column
	@Enumerated(value = EnumType.STRING)
	private WayOfPayment wayOfPayment;
	
	/*
	 * "rje za upozorenje@SuperBuilder will ignore the initializing expression entirely. 
	 * If you want the initializing expression to serve as default, add @Builder.Default. 
	 * If it is not supposed to be settable during building, make the field final."
	 * */
	@OneToMany(mappedBy = "magazine")
	@Builder.Default 
	private Set<Membership> memberships = new HashSet<Membership>();
	
	@ManyToOne
	@JoinColumn(name = "chief_editor_id")
	private UserSignedUp chiefEditor;
	
	@OneToMany(mappedBy = "magazine", fetch = FetchType.EAGER)
	@Builder.Default 
	private Set<EditorReviewerByScienceArea> editorsReviewersByScienceArea = new HashSet<EditorReviewerByScienceArea>();

//	@OneToMany(mappedBy = "magazine", fetch= FetchType.LAZY, cascade = CascadeType.ALL)
//	@OneToMany(mappedBy = "magazine")
//	@OneToMany(mappedBy = "magazine", fetch = FetchType.EAGER,
//		    cascade = {
//		            CascadeType.MERGE,
//		            CascadeType.PERSIST
//		        })
	//pravilo erore: JPA: detached entity passed to persist: nested exception is org.hibernate.PersistentObjectException
	@OneToMany(mappedBy = "magazine", fetch = FetchType.EAGER)
	private Set<MagazineEdition> magazineEditions;
	
	@Column
	private boolean active;
	
	@Column
	private Long membershipPrice;
	
	//zbog KP
//	private Long sellerIdentifier;

}
