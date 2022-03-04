package com.learning.dto;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cart")
public class Cart {
	
	@Id
	private Long userId;
	private Long cartId;
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinTable(name="user_cart", joinColumns=@JoinColumn(name="cartId"))
	private User User;
	private String status;
	@OneToMany(cascade=CascadeType.ALL)
	@JoinTable(name="food_cart", joinColumns=@JoinColumn(name="cartId"))
	private List<Food> foodCart;
	
}