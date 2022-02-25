package com.learning.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.dto.Address;
import com.learning.dto.Role;
import com.learning.dto.User;
import com.learning.enums.Roles;
import com.learning.exception.IdNotFoundException;
import com.learning.payload.request.LoginRequest;
import com.learning.payload.request.SignupRequest;
import com.learning.payload.response.JwtResponse;
import com.learning.repository.RoleRepository;
import com.learning.security.jwt.JwtUtils;
import com.learning.security.service.UserDetailsImpl;
import com.learning.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtUtils jwtUtils;
	
	
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
		user.setUsername(signupRequest.getName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		user.setRoles(roles);
		user.setDoj(signupRequest.getDoj());
		
		User addedUser = userService.addUser(user);
		
		return ResponseEntity.status(201).body(addedUser);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
		Authentication authentication = authenticationManager.
				authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
				
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken(authentication);
		
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();		
		
		List<String> roles = userDetailsImpl.getAuthorities().stream()
				.map(e-> e.getAuthority()).collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles));
	}
}
