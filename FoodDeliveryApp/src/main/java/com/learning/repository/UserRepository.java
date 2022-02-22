package com.learning.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.dto.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}
