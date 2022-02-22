package com.learning.service;

import java.util.List;
import java.util.Optional;

import com.learning.dto.User;

public interface UserService {
	public User addUser(User user);
	public Optional<User> getUserById(long id);
	public List<User> getAllUsers();
	public String deleteByUserId(long id);
	public User updateUser(User user);
	public List<User> getAllUsersAscOrder();
	public List<User> getAllUsersDescOrder();
	public boolean existsByUserId(long id);
}
