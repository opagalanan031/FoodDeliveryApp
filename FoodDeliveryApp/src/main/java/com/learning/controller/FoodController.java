package com.learning.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dto.Food;
import com.learning.enums.FoodType;
import com.learning.exception.DataNotFoundException;
import com.learning.payload.request.FoodRequest;
import com.learning.payload.response.FoodResponse;
import com.learning.repository.FoodRepository;

@Validated	// validates from inside the argument
@RestController	// combination of @Controller and @ResponseBody
@RequestMapping("/api/food")
public class FoodController {
	
	
	@Autowired
	FoodRepository foodRepository;
	
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addFood(@Valid @RequestBody Food food) {
		
		Food newFood = foodRepository.save(food);
		
		return ResponseEntity.status(201).body(newFood);
	}
	
	@GetMapping("/{foodId}")
	public ResponseEntity<?> getFoodById(@PathVariable("foodId") @Min(1) long foodId) {
		
		Food food = foodRepository.findById(foodId).orElseThrow(()-> new DataNotFoundException("no data found"));
		
		
		return ResponseEntity.ok(food);
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllFood() {
		List<Food> list = foodRepository.findAll();
		List<FoodResponse> foodResponses = new ArrayList<>();
		list.forEach(e->{
			FoodResponse foodResponse = new FoodResponse();
			foodResponse.setFoodName(e.getFoodName());
			foodResponse.setDescription(e.getDescription());
			foodResponse.setFoodCost(e.getFoodCost());
			foodResponse.setFoodPic(e.getFoodPic());
			foodResponse.setFoodType(e.getFoodType());
			foodResponses.add(foodResponse);
		});
		
		if(foodResponses.size()>0) {
			return ResponseEntity.ok(foodResponses);
		} else {
			throw new DataNotFoundException("no food available");
		}
	
	}
	
	@PutMapping("/{foodId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateFood(@Valid @PathVariable("foodId") long foodId, @RequestBody FoodRequest foodRequest)  {
		
		if(foodRepository.existsById(foodId)) {
			Food updatedFood = foodRepository.findById(foodId).get();
			
			updatedFood.setFoodName(foodRequest.getFoodName());
			updatedFood.setDescription(foodRequest.getDescription());
			updatedFood.setFoodCost(foodRequest.getFoodCost());
			updatedFood.setFoodPic(foodRequest.getFoodPic());
			updatedFood.setFoodType(foodRequest.getFoodType());
		
			foodRepository.save(updatedFood);
			return ResponseEntity.status(200).body(updatedFood);
		} else {
			throw new DataNotFoundException("record not found");
		}
		
	}
	
	@GetMapping("/type/{foodType}")
	public ResponseEntity<?> getFoodByType(@PathVariable("foodType") FoodType foodType) {
		
		List<Food> list = foodRepository.findByFoodType(foodType);
		List<FoodResponse> foodResponses = new ArrayList<>();
		list.forEach(e->{
			FoodResponse foodResponse = new FoodResponse();
			foodResponse.setFoodName(e.getFoodName());
			foodResponse.setDescription(e.getDescription());
			foodResponse.setFoodCost(e.getFoodCost());
			foodResponse.setFoodPic(e.getFoodPic());
			foodResponse.setFoodType(e.getFoodType());
			foodResponses.add(foodResponse);
		});
		
		if(foodResponses.size()>0) {
			return ResponseEntity.ok(foodResponses);
		} else {
			throw new DataNotFoundException("no food available");
		}
	
	}
	
	@DeleteMapping("/{foodId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteFoodById(@PathVariable("foodId") long foodId) {
		
		if(foodRepository.existsById(foodId)) {
			foodRepository.deleteById(foodId);
			return ResponseEntity.noContent().build();
		} else {
			throw new DataNotFoundException("record not found");
		}
		
	}
	
	@GetMapping("/desc")
	public ResponseEntity<?> getAllDescOrder() {
		List<Food> list = foodRepository.findAll();
		
		Collections.sort(list, (a,b)-> b.getFoodId().compareTo(a.getFoodId()));
		
		return ResponseEntity.status(200).body(list);
	}
	
	@GetMapping("/asc")
	public ResponseEntity<?> getAllAscOrder() {
		List<Food> list = foodRepository.findAll();
		
		Collections.sort(list, (a,b)-> a.getFoodId().compareTo(b.getFoodId()));
		
		return ResponseEntity.status(200).body(list);
	}
	
	
}
