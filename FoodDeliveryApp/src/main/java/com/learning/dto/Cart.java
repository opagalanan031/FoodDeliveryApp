//package com.learning.dto;
//
//import java.util.Set;
//
//import javax.persistence.CascadeType;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.OneToMany;
//import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name="cart")
//public class Cart {
//	
//	@Id
//	@NotNull
//	private Long userId;
//	@NotBlank
//	private String status;
//	@NotEmpty
//	@OneToMany(mappedBy="cart", cascade=CascadeType.ALL)
//	private Set<Food> foodCart;
//}
