package com.eurowings.subscriber.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eurowings.subscriber.model.Response;
import com.eurowings.subscriber.model.User;
import com.eurowings.subscriber.repository.UserRepository;

@RestController
@RequestMapping(value = "/api")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	/*
	 * 
	 * Checks if the given email id subscribed for newsletter or not.
	 * 
	 */
	@GetMapping("/receiveNewletter/{email}")
	public Response isNewsletterSubscribed(@PathVariable String email) {

		Response response = new Response();
		List<User> userList = null;
		User user = null;
		try {
			if (email.length() != 0 && email != null) {
				user = userRepository.findById(email).get();
				if (user != null) {
					userList = new ArrayList<User>();
					if (user.isNewsletterSubcribed() == true)
						response.setMessage(user.getEmailId() + " is subscribed to the newletter.");
					else
						response.setMessage(user.getEmailId() + " is not subscribed to the newletter.");
					response.setCode(HttpStatus.OK);
					userList.add(user);
					response.setUserList(userList);
				}
			} else {
				response.setCode(HttpStatus.BAD_REQUEST);
				response.setMessage("Illegal Arguments.");
			}
		} catch (NoSuchElementException e) {
			response.setCode(HttpStatus.NO_CONTENT);
			response.setMessage(email + " does not exist.");

		}
		return response;
	}

	/*
	 * 
	 * registration of the user. can be opt to receive newsletter
	 * 
	 */

	@PostMapping("/subscribe")
	public Response subscribeUser(@RequestBody User user) {
		Response response = new Response();
		List<User> userList = null;
		User tempUser = null;
		if (!userRepository.findById(user.getEmailId()).isPresent()) {
			tempUser = userRepository.save(user);
			if (tempUser != null) {
				userList = new ArrayList<User>();
				response.setCode(HttpStatus.CREATED);
				response.setMessage("User with email id " + tempUser.getEmailId() + "subscribed successfully.");
				userList.add(tempUser);
				response.setUserList(userList);
			} else {
				response.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
				response.setMessage("Subscription failed for user with " + user.getEmailId() + ". Please try again.");
			}
		} else {
			response.setCode(HttpStatus.FOUND);
			response.setMessage("User with email " + user.getEmailId() + " is already exist.");
		}
		return response;
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<List<User>> getAll() {

		return ResponseEntity.accepted().body(userRepository.findAll());

	}

	/*
	 * 
	 * Un-subscribing is equivalent to removing your account from the platform.
	 * 
	 */
	@DeleteMapping("/unsubscribe/{email}")
	public Response unsubscribe(@PathVariable String email) {
		Response response = new Response();
		List<User> userList = null;
		try {
			if (userRepository.findById(email).isPresent()) {
				if (email.length() != 0 && email != "") {
					userRepository.deleteById(email);
					if (!userRepository.findById(email).isPresent()) {
						response.setCode(HttpStatus.OK);
						response.setMessage("User with email " + email + " unsubscribed successfully.");
					}
				} else {
					response.setCode(HttpStatus.OK);
					response.setMessage("Illegal email.");
				}
			} else {
				response.setCode(HttpStatus.NOT_FOUND);
				response.setMessage("User with email " + email + " does not exist.");
			}
		} catch (IllegalArgumentException e) {
			response.setCode(HttpStatus.NOT_FOUND);
			response.setMessage("User with email " + email + " does not exist.");
		}
		return response;
	}
	/*
	 * 
	 * In case the user did not subcribed to newsletter initially but later on wants
	 * to receive the newsletter.
	 * 
	 */

	@PatchMapping("/subscribeToNewsletter/{email}")
	public Response subscribeNewsletter(@PathVariable String email) {
		Response response = new Response();
		List<User> userList = null;
		try {
			User user;
			boolean newletterSubscription;
			if (email != null && email != "") {
				user = userRepository.findById(email).get();
				if (user != null) {
					userList = new ArrayList<User>();
					newletterSubscription = user.isNewsletterSubcribed();
					if (newletterSubscription == false) {
						user.setNewsletterSubcribed(true);
						userRepository.save(user);
						response.setCode(HttpStatus.OK);
						response.setMessage("User with email " + email + " successfully subscribed to newsletter.");

					} else {
						response.setCode(HttpStatus.METHOD_NOT_ALLOWED);
						response.setMessage("User with email " + email + " is already subscribed to newsletter.");
					}
					userList.add(user);
					response.setUserList(userList);

				} else {
					response.setCode(HttpStatus.NOT_FOUND);
					response.setMessage("User with email " + email + " does not exist.");
				}
			} else {
				response.setCode(HttpStatus.BAD_REQUEST);
				response.setMessage("Improper Email.");
			}
		} // try
		catch (NoSuchElementException e) {
			response.setCode(HttpStatus.BAD_REQUEST);
			response.setMessage("User with email does not exist.");

		} catch (IllegalArgumentException e) {
			response.setCode(HttpStatus.BAD_REQUEST);
			response.setMessage("Illegal argument ");

		}
		return response;

	}

	@GetMapping("/getUserBefore/{date}")
	public Response usersBefore(@PathVariable String date) {
		
		Response response = new Response();
		
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		List<User> userList = new ArrayList<User>();
		

		try {
			Date beforeDate = sdfrmt.parse(date);
			if (beforeDate != null) {
				userList=userRepository.findBySubscriptionDateBefore(beforeDate);
				if(!userList.isEmpty()) {
					response.setCode(HttpStatus.OK);
					response.setMessage(userList.size() + " users found.");
				}
			}
		} catch (ParseException e) {
			response.setCode(HttpStatus.BAD_REQUEST);
			response.setMessage("Enter the date in yyyy-MM-dd format.");
		}
		
		response.setUserList(userList);
		return response;
	}

	@GetMapping("/getUserAfter/{date}")
	public Response usersAfter(@PathVariable String date) {

		Response response = new Response();

		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		List<User> userList = new ArrayList<User>();

		try {
			Date afterDate = sdfrmt.parse(date);
			if (afterDate != null) {
				userList = userRepository.findBySubscriptionDateAfter(afterDate);
				if (!userList.isEmpty()) {
					response.setCode(HttpStatus.OK);
					response.setMessage(userList.size() + " users found.");
					response.setUserList(userList);
					}
				else {
					response.setCode(HttpStatus.OK);
					response.setMessage("No users found.");
					}
			}
		} catch (ParseException e) {
			response.setCode(HttpStatus.BAD_REQUEST);
			response.setMessage("Enter the date in yyyy-MM-dd format.");
		}
		
		return response;
	}

}
