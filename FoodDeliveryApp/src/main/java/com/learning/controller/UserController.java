package com.learning.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.learning.dto.Address;
//import com.learning.dto.Cart;
import com.learning.dto.Food;
import com.learning.dto.Role;
import com.learning.dto.User;
import com.learning.enums.Roles;
import com.learning.exception.DataNotFoundException;
import com.learning.exception.IdNotFoundException;
//import com.learning.payload.request.CartRequest;
//import com.learning.payload.request.LoginRequest;
import com.learning.payload.request.SignupRequest;
import com.learning.payload.response.UserResponse;
//import com.learning.repository.CartRepository;
import com.learning.repository.FoodRepository;
import com.learning.repository.RoleRepository;
import com.learning.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
		
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private FoodRepository foodRepository;
	//@Autowired
	//private CartRepository cartRepository;
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers() {
		List<User> list = userService.getAllUsers();
		List<UserResponse> userResponses = new ArrayList<>();
		list.forEach(e->{
			UserResponse userResponse = new UserResponse();
			userResponse.setEmail(e.getEmail());
			userResponse.setName(e.getUsername());
			userResponse.setDoj(e.getDoj());
			Set<String> roles = new HashSet<>();
			e.getRoles().forEach(e1->{
				roles.add(e1.getRoleName().name());
			});
			Set<com.learning.payload.request.Address> addresses = new HashSet<>();
			e.getAddresses().forEach(e2->{
				com.learning.payload.request.Address address = new com.learning.payload.request.Address();
				address.setHouseNo(e2.getHouseNo());
				address.setStreet(e2.getStreet());
				address.setCity(e2.getCity());
				address.setState(e2.getState());
				address.setCountry(e2.getCountry());
				address.setZip(e2.getZip());
				addresses.add(address);
			});
			userResponse.setRoles(roles);
			userResponse.setAddress(addresses);
			userResponses.add(userResponse);
		});
		if(userResponses.size()>0) {
			return ResponseEntity.ok(userResponses);
		} else {
			throw new DataNotFoundException("no users are available");
		}
		
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable("userId") long userId) {
		
		User user =	userService.getUserById(userId).orElseThrow(()->new DataNotFoundException("data not available"));
	
		UserResponse userResponse=  new UserResponse();
		userResponse.setEmail(user.getEmail());
		userResponse.setName(user.getUsername());
		Set<String> roles= new HashSet<>();
		userResponse.setDoj(user.getDoj());
		user.getRoles().forEach(e1->{
			roles.add(e1.getRoleName().name());
		});
		Set<com.learning.payload.request.Address> addresses = new HashSet<>();
		user.getAddresses().forEach(e2->{
			com.learning.payload.request.Address address = new com.learning.payload.request.Address();
			address.setHouseNo(e2.getHouseNo());
			address.setCity(e2.getCity());
			address.setCountry(e2.getCountry());
			address.setState(e2.getState());
			address.setStreet(e2.getStreet());
			address.setZip(e2.getZip());
			addresses.add(address);
		});
		userResponse.setAddress(addresses);
		userResponse.setRoles(roles);
		return ResponseEntity.status(200).body(userResponse);
	}
	
	@PutMapping("/{userId}")
	public ResponseEntity<?> updateUser(@PathVariable("userId") long userId, @RequestBody SignupRequest signupRequest) {
			
		if(userService.existsByUserId(userId)) {			
			
			User user = userService.getUserById(userId).get();
			
			Set<Address> addresses = new HashSet<>();
			signupRequest.getAddress().forEach(e->{
				Address address = new Address();
				address.setHouseNo(e.getHouseNo());
				address.setStreet(e.getStreet());
				address.setCity(e.getCity());
				address.setState(e.getState());
				address.setCountry(e.getCountry());
				address.setZip(e.getZip());
				address.setUser(user);
				addresses.add(address);
			});
			
			user.setAddresses(addresses);
			user.setUsername(signupRequest.getName());
			user.setEmail(signupRequest.getEmail());
			user.setPassword(signupRequest.getPassword());
			//user.setRoles(roles);
			user.setDoj(signupRequest.getDoj());
			
			User updatedUser = userService.updateUser(user);
			
			return ResponseEntity.status(200).body(updatedUser);
		} else {
			throw new DataNotFoundException("record not found");
		}
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUserById(@PathVariable("userId") long userId)  {
		
		// check if user exists or not
		if(userService.existsByUserId(userId)) {
			userService.deleteByUserId(userId);
			return ResponseEntity.noContent().build();
		} else {
			throw new DataNotFoundException("record not found");
		}
		
	}
	
	@GetMapping("/desc")
	public ResponseEntity<?> getAllDescOrder() {
		List<User> list = userService.getAllUsersDescOrder();
		
		Collections.sort(list, (a,b)-> b.getUserId().compareTo(a.getUserId()));
		
		return ResponseEntity.status(200).body(list);
	}
	
	@GetMapping("/asc")
	public ResponseEntity<?> getAllAscOrder() {
		List<User> list = userService.getAllUsersAscOrder();
		
		Collections.sort(list, (a,b)-> a.getUserId().compareTo(b.getUserId()));
		
		return ResponseEntity.status(200).body(list);
	}
	
//	@PutMapping("/{userId}/cart")
//	public ResponseEntity<?> addToCart(@PathVariable("userId") long userId, CartRequest cartRequest) {
//		
//		User user = userService.getUserById(userId).get();
//		List<Food> foods = foodRepository.findAll();
//		Cart cart = new Cart();
//		
//		Set<Food> foodCart = new HashSet<>();
//		cartRequest.getFoodCart().forEach(e->{
//			for(int i = 0; i < foods.size(); i++) {
//				if(e.equals(foods.get(i).getFoodName())) {
//					foodCart.add(foods.get(i));
//				}
//			}
//			
//		});
//		
//		cart.setUserId(user.getUserId());
//		cart.setFoodCart(foodCart);
//		cart.setStatus("active");
//		
//		cartRepository.save(cart);
//		
//		List<String> list = new ArrayList<>();
//		cart.getFoodCart().forEach(e->{
//			list.add(e.getFoodName());
//		});
//		
//		return ResponseEntity.status(200).body(list);
//	}
	
	
}
