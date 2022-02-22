package com.learning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.dto.Food;
import com.learning.enums.FoodType;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
	public List<Food> findByFoodType(FoodType foodType); 
}