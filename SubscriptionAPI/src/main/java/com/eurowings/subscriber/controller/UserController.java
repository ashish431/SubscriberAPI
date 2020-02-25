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
	@GetMapping("/reciveNewletter/{email}")
	public Response isNewsletterSubscribed(@PathVariable String email) {

		Response response = new Response();
		HttpStatus code;
		String message;
		List<User> userList = null;
		User user = null;
		// System.out.println(email);
		try {
			if (email.length() != 0 && email != null) {
				user = userRepository.findById(email).get();
				System.out.println("User : " + user.getEmailId());
				if (user != null) {
					userList = new ArrayList<User>();
					if (user.isNewsletterSubcribed() == true)
						message = user.getEmailId() + " is subscribed to the newletter.";
					else
						message = user.getEmailId() + " is not subscribed to the newletter.";
					code = HttpStatus.OK;
					userList.add(user);
				} else {
					code = HttpStatus.NO_CONTENT;
					message = "No such user";
				}
			} else {
				code = HttpStatus.BAD_REQUEST;
				message = "Illegal Arguments.";
				// return new ResponseEntity("", HttpStatus.BAD_REQUEST);
			}
		} catch (NoSuchElementException e) {
			code = HttpStatus.NO_CONTENT;
			message = email + " does not exist.";

		}
		response.setCode(code);
		response.setMessage(message);
		response.setUserList(userList);
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
		HttpStatus code;
		String message;
		List<User> userList = null;
		User u = null;
		if (!userRepository.findById(user.getEmailId()).isPresent()) {
			System.out.println(user.toString());
			u = userRepository.save(user);
			if (u != null) {
				userList = new ArrayList<User>();
				code = HttpStatus.CREATED;
				message = "User with email id " + u.getEmailId() + "subscribed successfully.";
				userList.add(u);
			} else {
				code = HttpStatus.INTERNAL_SERVER_ERROR;
				message = "Subscription failed for user with " + u.getEmailId() + " Please try again.";
			}
		} else {
			code = HttpStatus.FOUND;
			message = "User with email " + user.getEmailId() + " is already exist.";
		}
		response.setCode(code);
		response.setMessage(message);
		response.setUserList(userList);
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
		HttpStatus code = null;
		String message = null;
		List<User> userList = null;
		try {
			if (userRepository.findById(email).isPresent()) {
				if (email.length() != 0 && email != "") {
					userRepository.deleteById(email);
					if (!userRepository.findById(email).isPresent()) {
						code = HttpStatus.OK;
						message = "User with email " + email + " unsubscribed successfully.";
					}
				} else {
					code = HttpStatus.OK;
					message = "Illegal email.";
				}
			} else {
				code = HttpStatus.NOT_FOUND;
				message = "User with email " + email + " does not exist.";
			}
		} catch (IllegalArgumentException e) {
			code = HttpStatus.NOT_FOUND;
			message = "User with email " + email + " does not exist.";
		}
		response.setCode(code);
		response.setMessage(message);
		response.setUserList(userList);
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
		HttpStatus code = null;
		String message = null;
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
						code = HttpStatus.OK;
						message = "User with email " + email + " successfully subscribed to newsletter.";

					} else {
						code = HttpStatus.METHOD_NOT_ALLOWED;
						message = "User with email " + email + " is already subscribed to newsletter.";
					}
					userList.add(user);

				} else {
					code=HttpStatus.NOT_FOUND;
					message="User with email " + email + " does not exist.";
				}
			} else {
				code=HttpStatus.BAD_REQUEST;
				message="Improper Email.";
			}
		} // try
		catch (NoSuchElementException e) {
			code = HttpStatus.BAD_REQUEST;
			message = "User with email does not exist.";

		} catch (IllegalArgumentException e) {
			code = HttpStatus.BAD_REQUEST;
			message = "Illegal argument ";

		}
		response.setCode(code);
		response.setMessage(message);
		response.setUserList(userList);
		return response;

	}

	@GetMapping("/getUserBefore/{date}")
	public Response usersBefore(@PathVariable String date) {
		
		Response response = new Response();
		HttpStatus code = null;
		String message = null;
		
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		List<User> userList = new ArrayList<User>();
		

		try {
			Date beforeDate = sdfrmt.parse(date);
			if (beforeDate != null) {
				userList=userRepository.findBySubscriptionDateBefore(beforeDate);
				if(!userList.isEmpty()) {
					code=HttpStatus.OK;
					message=userList.size() + " users found.";
				}
			}
		} catch (ParseException e) {
			code=HttpStatus.BAD_REQUEST;
			message="Enter the date in yyyy-MM-dd format.";
		}
		
		response.setCode(code);
		response.setMessage(message);
		response.setUserList(userList);
		return response;
	}

	@GetMapping("/getUserAfter/{date}")
	public Response usersAfter(@PathVariable String date) {

		Response response = new Response();
		HttpStatus code = null;
		String message = null;
		
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		List<User> userList = new ArrayList<User>();

		try {
			System.out.println("after");
			Date afterDate = sdfrmt.parse(date);
			System.out.println(" Date: "+afterDate);
			if (afterDate != null) {
				userList = userRepository.findBySubscriptionDateAfter(afterDate);
				if (!userList.isEmpty()) {
					code=HttpStatus.OK;
					message=userList.size() + " users found.";
				}
			}
		} catch (ParseException e) {
			code=HttpStatus.BAD_REQUEST;
			message="Enter the date in yyyy-MM-dd format.";
		}
		
		
		response.setCode(code);
		response.setMessage(message);
		response.setUserList(userList);
		return response;
	}

}
