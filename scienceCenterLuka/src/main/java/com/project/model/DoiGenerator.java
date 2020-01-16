package com.project.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

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
public class DoiGenerator {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idGenerator")
	@GenericGenerator(name = "idGenerator",
	    strategy = "com.project.util.IdGenerator",
	    parameters = {
	        @Parameter(name = "sequence", value = "doi_generator_doi_generator_id_sequence")
	    })
	private Integer generatedId;
	
	@OneToOne
	private Article article;
}
