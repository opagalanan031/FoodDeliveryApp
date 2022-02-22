package com.learning.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.dto.User;
import com.learning.repository.UserRepository;
import com.learning.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public Optional<User> getUserById(long id) {
		return userRepository.findById(id);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public String deleteByUserId(long id) {
		userRepository.deleteById(id);
		return "success";
	}

	@Override
	public User updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsersAscOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getAllUsersDescOrder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsByUserId(long id) {
		return userRepository.existsById(id);
	}
	
	

}
