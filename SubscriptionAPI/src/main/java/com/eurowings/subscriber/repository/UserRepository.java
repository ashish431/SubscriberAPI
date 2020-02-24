package com.eurowings.subscriber.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.eurowings.subscriber.model.User;



@Repository
public interface UserRepository extends JpaRepository<User, String> {

}
