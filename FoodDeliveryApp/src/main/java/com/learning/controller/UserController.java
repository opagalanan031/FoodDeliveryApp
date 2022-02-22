package com.learning.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.learning.dto.Role;
import com.learning.dto.User;
import com.learning.enums.Roles;
import com.learning.exception.DataNotFoundException;
import com.learning.exception.IdNotFoundException;
//import com.learning.payload.request.LoginRequest;
import com.learning.payload.request.SignupRequest;
import com.learning.payload.response.UserResponse;
import com.learning.repository.RoleRepository;
import com.learning.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
		
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) throws IdNotFoundException { 
		Set<Role> roles = new HashSet<>();
		
		if(signupRequest.getRoles()==null) {
			Role userRole = roleRepository.findByRoleName(Roles.ROLE_USER)
			.orElseThrow(() -> new IdNotFoundException("RoleId not found exception"));
			roles.add(userRole);
		} else {
			signupRequest.getRoles().forEach(e->{
				
				switch (e) {
				case "user":
					Role userRole = roleRepository.findByRoleName(Roles.ROLE_USER)
					.orElseThrow(() -> new IdNotFoundException("RoleId not found exception"));
					roles.add(userRole);
					break;
				case "admin":
					Role adminRole = roleRepository.findByRoleName(Roles.ROLE_ADMIN)
					.orElseThrow(() -> new IdNotFoundException("RoleId not found exception"));
					roles.add(adminRole);
					break;

				default:
					break;
				}

			});
		}
		
		User user = new User();
		
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
		user.setName(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(signupRequest.getPassword());
		user.setRoles(roles);
		user.setDoj(signupRequest.getDoj());
		
		User addedUser = userService.addUser(user);
		
		return ResponseEntity.status(201).body(addedUser);
	}
	
	
	//@PostMapping("/authenticate")
	//public boolean authenticate(@RequestBody LoginRequest loginRequest) {
	//	return null;
	//}
	
	@GetMapping("/")
	public ResponseEntity<?> getAllUsers() {
		List<User> list = userService.getAllUsers();
		List<UserResponse> userResponses = new ArrayList<>();
		list.forEach(e->{
			UserResponse userResponse = new UserResponse();
			userResponse.setEmail(e.getEmail());
			userResponse.setName(e.getName());
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
		userResponse.setName(user.getName());
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
	
	//@PutMapping("/users/update/{userId}")
	//public User updateUser(@PathVariable("userId") long userId, @RequestBody User newUser) throws UserNotFoundException {
		
	//	return userRepo.findById(userId)
	//			.map(user -> {
	//				Address address = new Address();
					
					//user.setName(newUser.getName());
					//user.setEmail(newUser.getEmail());
					//user.setPassword(newUser.getPassword());
					//user.setAddresses.(address);
					//user.getAddresses().setHouseNo(newUser.getAddress().getHouseNo());
					//user.getAddress().setStreet(newUser.getAddress().getStreet());
					//user.getAddress().setCity(newUser.getAddress().getCity());
					//user.getAddress().setState(newUser.getAddress().getState());
					//user.getAddress().setZip(newUser.getAddress().getZip());
		//			return userRepo.save(user);
		//		}).orElseGet(() -> {
		//			newUser.setUserId(userId);
		//			return userRepo.save(newUser);
		//		});
	//}
	
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
		
	
	
	
}
