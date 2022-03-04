package com.learning.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dto.Address;
import com.learning.dto.Cart;
import com.learning.dto.Food;
import com.learning.dto.User;
import com.learning.exception.DataNotFoundException;
import com.learning.payload.request.CartRequest;
import com.learning.payload.request.UpdateUserRequest;
import com.learning.payload.response.UserResponse;
import com.learning.repository.CartRepository;
import com.learning.repository.FoodRepository;
import com.learning.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
		
	@Autowired
	private UserService userService;
	@Autowired
	private FoodRepository foodRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN')")
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
	@PreAuthorize("hasRole('ADMIN','USER')")
	public ResponseEntity<?> updateUser(@Valid @PathVariable("userId") long userId, @RequestBody UpdateUserRequest updateUserRequest) {
			
		if(userService.existsByUserId(userId)) {			
			
			User user = userService.getUserById(userId).get();
			
			Set<Address> addresses = new HashSet<>();
			updateUserRequest.getAddress().forEach(e->{
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
			
			user.setUsername(updateUserRequest.getName());
			user.setEmail(updateUserRequest.getEmail());
			user.setPassword(passwordEncoder.encode(updateUserRequest.getPassword()));
			
			User updatedUser = userService.updateUser(user);
			
			return ResponseEntity.status(200).body(updatedUser);
		} else {
			throw new DataNotFoundException("record not found");
		}
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ADMIN', 'USER')")
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
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllDescOrder() {
		List<User> list = userService.getAllUsersDescOrder();
		
		Collections.sort(list, (a,b)-> b.getUserId().compareTo(a.getUserId()));
		
		return ResponseEntity.status(200).body(list);
	}
	
	@GetMapping("/asc")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllAscOrder() {
		List<User> list = userService.getAllUsersAscOrder();
		
		Collections.sort(list, (a,b)-> a.getUserId().compareTo(b.getUserId()));
		
		return ResponseEntity.status(200).body(list);
	}
	
	@PutMapping("/{userId}/cart")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addToCart(@Valid @PathVariable("userId") long userId, @RequestBody CartRequest cartRequest) {
		
		if(userService.existsByUserId(userId)) {
			User user = userService.getUserById(userId).get();
			List<Food> foods = foodRepository.findAll();
			Cart cart = new Cart();
			
			List<Food> foodCart = new LinkedList<>();
			cartRequest.getFoodCart().forEach(e->{
				for(int i = 0; i < foods.size(); i++) {
					if(e.equals(foods.get(i).getFoodName())) {
						foodCart.add(foods.get(i));
					}
				}
				
			});
			
			cart.setUserId(userId);
			cart.setCartId(userId);
			cart.setUser(user);
			cart.setFoodCart(foodCart);
			cart.setStatus("active");
			
			cartRepository.save(cart);
			
			List<String> list = new ArrayList<>();
			cart.getFoodCart().forEach(e->{
				list.add(e.getFoodName());
			});
			
			return ResponseEntity.status(200).body(list);
		} else {
			throw new DataNotFoundException("record not found");
		}
		
		
	}
	
	@PutMapping("/{userId}/cart/checkout")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> checkout(@Valid @PathVariable("userId") long userId, @RequestBody CartRequest cartRequest) {
		
		if(userService.existsByUserId(userId)) {
			Cart cart = cartRepository.getById(userId);
			
			List<Food> foodCart = cart.getFoodCart();
			cartRequest.getFoodCart().forEach(e->{
				for(int i = 0; i < foodCart.size(); i++) {
					if(e.equals(foodCart.get(i).getFoodName())) {
						foodCart.remove(i);
					}
				}
				
			});
			
			cart.setStatus("not active");
			
			cartRepository.save(cart);
			
			
			return ResponseEntity.status(200).body(cart.getFoodCart());
		} else {
			throw new DataNotFoundException("record not found");
		}
		
		
	}
	
	
}
