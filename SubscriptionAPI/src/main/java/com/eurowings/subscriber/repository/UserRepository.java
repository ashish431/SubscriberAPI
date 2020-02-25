package com.eurowings.subscriber.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eurowings.subscriber.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
	List<User> findBySubscriptionDateBefore(Date date);
	List<User> findBySubscriptionDateAfter(Date date);

}
