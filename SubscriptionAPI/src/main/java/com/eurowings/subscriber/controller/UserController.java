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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<String> isNewletterSubscribed(@PathVariable String email) {

		User user = null;
		// System.out.println(email);
		if (email.length() != 0 && email != null) {
			user = userRepository.findById(email).get();
			System.out.println("User : " + user.getEmailId());
			if (user != null)
				return new ResponseEntity(user.isNewsletterSubcribed() + "", HttpStatus.OK);
			else
				return new ResponseEntity("No such user.", HttpStatus.NO_CONTENT);
		} else
			return new ResponseEntity("", HttpStatus.BAD_REQUEST);

	}

	/*
	 * 
	 * registration of the user. can be opt to receive newsletter
	 * 
	 */

	@PostMapping("/subscribe")
	public ResponseEntity<String> subscribeUser(@RequestBody User user) {
		User u=null;
		if (!userRepository.findById(user.getEmailId()).isPresent()) {
			System.out.println(user.toString());
			u = userRepository.save(user);
			if (u != null)
				return new ResponseEntity("User with "+u.getEmailId()+"Subscribed Successfully.", HttpStatus.CREATED);
			else
				return new ResponseEntity("Subscription failed for user with "+u.getEmailId()+" Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
		} else
			return new ResponseEntity("User with email "+u.getEmailId()+".", HttpStatus.NOT_FOUND);
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
	public ResponseEntity<String> unsubscribe(@PathVariable String email) {
		if (!userRepository.findById(email).isPresent()) {
			if (email.length() != 0 && email != "") {
				userRepository.deleteById(email);
				return new ResponseEntity("User with email "+ email +" unsubscribed successfully.", HttpStatus.OK);
			} else
				return new ResponseEntity("Unable to unsubscribe user now. Please try again.",
						HttpStatus.INTERNAL_SERVER_ERROR);
			

		} else
			return new ResponseEntity("User with email "+email+" does not exist.", HttpStatus.NOT_FOUND);
	}
	/*
	 * 
	 * In case the user did not subcribed to newsletter initially but later on wants
	 * to receive the newsletter.
	 * 
	 */

	@PatchMapping("/subscribeNewsletter/{email}")
	public ResponseEntity<String> subscribeNewsletter(@PathVariable String email) {
		try {
			User user;
			boolean newletterSubscription;
			if (email != null && email != "") {
				user = userRepository.findById(email).get();
				if (user != null) {
					newletterSubscription = user.isNewsletterSubcribed();
					if (newletterSubscription == false) {
						user.setNewsletterSubcribed(true);
						userRepository.save(user);
						return new ResponseEntity("User with email "+email+" successfully subscribed to newsletter.", HttpStatus.OK);
					} else {
						return new ResponseEntity("User is already subscribed to newsletter.", HttpStatus.METHOD_NOT_ALLOWED);
					}

				} else
					return new ResponseEntity("User with email "+email+" does not exist.", HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity("Improper Email.", HttpStatus.BAD_REQUEST);
			}
		} // try
		catch (NoSuchElementException e) {
			return new ResponseEntity("User with email does not exist.", HttpStatus.BAD_REQUEST);
		}
		catch(IllegalArgumentException e)
		{
			return new ResponseEntity("Illegal argument "+email+".", HttpStatus.BAD_REQUEST);
		}
		
	}

	@GetMapping("/getUserBefore/{date}")
	public ResponseEntity<List<User>> usersBefore(@PathVariable String date) {
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		List<User> userList = new ArrayList<User>();
		List<User> result = null;
		try {
			Date beforeDate = sdfrmt.parse(date);
			if (beforeDate != null) {
				userList = userRepository.findAll();
				if (!userList.isEmpty()) {
					result=new ArrayList<User>();
					for (User user : userList) {
						Date objDate = user.getSubscriptionDate();
						if (objDate.before(beforeDate)) {
							result.add(user);
						}
					}
					return ResponseEntity.accepted().body(result);
				}
			}
		} catch (ParseException e) {
			return new ResponseEntity("Enter the date in yyyy-MM-dd format.",HttpStatus.BAD_GATEWAY);
		}
		
		
		return (result==null)?new ResponseEntity("User before "+date+" does not exist.",HttpStatus.NOT_FOUND): ResponseEntity.accepted().body(result);
	}

	@GetMapping("/getUserAfter/{date}")
	public ResponseEntity<List<User>> usersAfter(@PathVariable String date) {
		SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
		List<User> userList = new ArrayList<User>();
		List<User> result = new ArrayList<User>();
		try {
			Date beforeDate = sdfrmt.parse(date);
			if (beforeDate != null) {
				userList = userRepository.findAll();
				if (!userList.isEmpty()) {
					for (User user : userList) {
						Date objDate = user.getSubscriptionDate();
						if (objDate.after(beforeDate)) {
							result.add(user);
						}
					}
					return ResponseEntity.accepted().body(result);
				}
			}
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
		return (result==null)?new ResponseEntity("User after "+date+" does not exist.",HttpStatus.NOT_FOUND): ResponseEntity.accepted().body(result);
	}

}
