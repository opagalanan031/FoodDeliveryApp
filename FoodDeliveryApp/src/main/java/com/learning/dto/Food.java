package com.learning.dto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.learning.enums.FoodType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="food")
public class Food {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long foodId;
	@NotBlank
	private String foodName;
	@NotBlank
	private String description;
	@NotNull
	private float foodCost;
	@NotBlank
	private String foodPic;
	
	@Enumerated(EnumType.STRING)
	private FoodType foodType;
	
}
