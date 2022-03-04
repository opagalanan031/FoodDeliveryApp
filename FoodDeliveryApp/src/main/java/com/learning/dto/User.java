package com.learning.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data

@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude={"addresses", "roles"})
@ToString(exclude={"addresses", "roles"})
@Table(name="user",
uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long userId;
	private String username;
	private String email;
	private String password;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate doj = LocalDate.now();
	
	// cascade: includes effects to associated entities 
	// fetch: when to load the associated data, always or when demanded
	@JsonIgnore
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.LAZY) 
	private Set<Address> addresses;
	
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="user_roles", 
	joinColumns=@JoinColumn(name="user_id"),
	inverseJoinColumns=@JoinColumn(name="id"))
	public Set<Role> roles;
	

}
