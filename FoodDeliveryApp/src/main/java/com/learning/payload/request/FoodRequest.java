package com.learning.payload.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.learning.enums.FoodType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {

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
