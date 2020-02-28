# Subscription API

**API Overview** :

The API is created to allow the user subscribe to the service i.e. registering to the platform. The users can decide whether they want to subscribe to the newsletter or not at the time of registration. The user can always subscribe/ unsubscribe to the newsletter.

**Database Details:**

Database used for storing the user information is called **H2**. It is one of the popular in-memory databases written in Java. It can be embedded in Java applications or run in the client-server mode. Despite being an in-memory database itprovides a console view to maintain and interact with the database tables and data.

**Assumptions:**

1. All the API calls are authenticated.
2. User data will never be deleted.
3. There exist only one kind of newsletter to which user can subscribe.

**Future Enhancements:**

1. Authentication support can be added to make the API calls valid.
2. Response can be compressed using Gzip. It will save bandwidth and increase response time.
3. Add rate limit on API call to avoid DOS attacks.
4. Caching content to reduce server side load.
5. Single user can subscribe to multiple type of newsletter (subject wise).
6. Database can be run in master slave mode to increase availability.

**API end points:**

1. **/receiveNewletter/{email}**

  **Http Method:** GET

  **Description:** Tests whether the passed email should receive the newsletter or not.

  **Parameter:** Email to be tested has to be passed in the in the URL.

  **JSON Response:**

   When user is subscribe to newsletter:
   
    http://localhost:8080/api/receiveNewletter/jack@gmail.com

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/1.png)

When user is not subscribed to newsletter:

    http://localhost:8080/api/receiveNewletter/mark@gmail.com

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/2.png)

2. **/getUserBefore/{date}**

  **Http Method:** GET

  **Description:** Returns the list of users who subscribed before the passed date.

  **Parameter:** Date in &quot;yyyy-MM-dd&quot; format.

  **JSON Response:**

   When the users before the given date is present.

    http://localhost:8080/api/getUserBefore/2018-01-01

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/3.png)

  When no users is present before the given date.

    http://localhost:8080/api/getUserBefore/2017-01-01

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/4.png)

3. **/getUserAfter/{date}**

  **Http Method:** GET

  **Description:** Returns the list of users who subscribed before the passed date.

  **Parameter:** Date in &quot;yyyy-MM-dd&quot; format.

  **JSON Response:**

  When the users are present after the given date.

    http://localhost:8080/api/getUserAfter/2020-01-01

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/5.png)

  When no users is present after the given date.

    http://localhost:8080/api/getUserAfter/2020-02-28

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/6.png)

4. **/subscribe**

  **Http Method:** POST

  **Description** : Add the user to the database of the subscribed users.

  **Parameter** : Request body in JSON format.

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/7.png)

  **JSON Response:**

  When the user is successfully subscribed:

     http://localhost:8080/api/subscribe

  **Request Body**

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/8.png)

  **Response:**

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/9.png)

  When the user is already present in the database:

**Request Body:**

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/10.png)

**Response:**

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/11.png)

5. **/unsubscribeNewsletter/{email}**

  **Http Method:** PATCH

  **Description:** It is used to set the newsletter subscription flag to false. The user with email id passed as the parameter will stop receiving newsletters.

  **Parameter:** Email has to be passed in the in the URL.

  **JSON Response:**

  When user successfully unsubscribe the newsletter:

    http://localhost:8080/api/unsubscribeNewsletter/dennis@gmail.com

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/12.png)

  When the user is already unsubscribed to the newsletter.

    http://localhost:8080/api/unsubscribeNewsletter/dennis@gmail.com

 ![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/13.png)

6. **/subscribeToNewsletter/{email}**

**Http Method:** PATCH

**Description:** It&#39;s used to subscribe to the newsletter. It marks the newsletter subscription flag to true.

**Parameter :** Email of the user.

**JSON Response:**

When user successfully subscribe to the newsletter.

    http://localhost:8080/api/subscribeToNewsletter/ashish@gmail.com

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/14.png)

When user is already subscribe to the newsletter.

     http://localhost:8080/api/subscribeToNewsletter/ashish@gmail.com

![alt text](https://github.com/ashish431/SubscriberAPI/blob/master/SubscriptionAPI/src/main/resources/images/15.png)
