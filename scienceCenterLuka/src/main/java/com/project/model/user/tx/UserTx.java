package com.project.model.user.tx;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.project.model.enums.TxStatus;
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
public class UserTx {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long userTxId;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private UserSignedUp user;
	
	@Column
	private Date created;
	
	@Column
	@Enumerated(EnumType.STRING)
	private TxStatus status;
	
	@OneToMany(mappedBy = "userTx", fetch = FetchType.EAGER)
	private Set<UserTxItem> items;
	
	@Column
	private Float totalAmount;
	
	@Column
	private Long kPClientIdentifier;
	
	@Column
	private Long orderId;
	
	@Column
	private String procId;
	
}
