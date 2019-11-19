# SOCIAL NETWORK for Travellers 
### (by Yavor Yankov and Tihomira Tacheva)

**SOCIAL NETWORK for Travellers** is web application which enables its users to connect with people, create, edit and delete posts, comments and replies of a comment, get a feed of the newest posts of their connections and to like as well all posts, comments and replies that they view in their newsfeed.

## Getting Started
Anyone who go to the application landing page can view registration form or login form, user and posts field in the app where any unregistered user can search on typing in the field in the top of the page.

##### If the user wants more rights, he needs to register in the application with the button "Sign up" or if he has already registered click "Already have an account?" and log in to his account through the button "Sign in".
When registering, the user must complete his main features which he can then modify after that to his own "My Info" page, using the buttin "Update profile": 

- **username** of the user;
- **email** of the user;
- **password** of the user;
- **password confirmation**;
- **first name** of the user;
- **last name** of the user;
- upload of **personal photo** (optional);
- upload of **cover photo** (optional);
- **public** or **private** visibility of user's names;
- **public** or **private** visibility of user's personal photo;


Once logged in, each user is already authorized to go to the feed of newest posts (**"Newsfeed"**), add new ones, add comments of existing posts or reply to other comments. At the top of the page, the user can add a new post by filling in the content of the post, as well as optionally adding a photo, video and location where he/ she is. The post can be posted via the button **Publish**. Also the user is able to manage his/her posts/comments and replies by modifying their content and deleting them. This can be done by clicking wrench and trash icons. By clicking on the heart icon, the user can like a post, comment or reply. To the right of the page, the user receives suggestions for other users to whom he/she can send a friend request. 
Going into **"Image"**, the registered user has options to views images from the post's feed and their authors, also can like each post. The friendship suggestions section can also be found on this page. 
In addition, he user gets the right to connect with other travellers and he/she can find his/her friends on the page **"My friends"**. He/she can access their pages by clicking on their profile name. 

Each **_registered user_** through the **"My profile"**-**"My info"** to the navbar has access to their own profile information, also how many posts and friends he/she has. On the **My info** page there is as well an **"Update"** button with which he can modify his own data, as well as **"Delete"** that can remove its own account from the application. Each user on his own profile page can find information about himself:
- username;
- email;
- first name;
- last name;
- personal photo;
- cover photo;
- count of his/her own posts;
- count of friends;

To **"My info"** the user can view and manage his own posts. Also, to the right of the page, he can see all of his friendship invitations and accept or reject them accordingly. The same thing can be done on the inviting user's page. If they are already friends, the user can respectively block or unblock the user. 
Going to **"My profile"**-**"My albums"** the user can see an album of pictures that he/she has uploaded to the app. To **"My profile"**-**"My friends"** the user can see his/her friends.
**_Administrators_** have access to all _registered users_ through the **"Admin"** panel, including their own account. **Administrators** have the same options and in addition to the common they can access, modify and delete other users accounts from the application's system. The administrator also has the right to update and delete posts, comments and replies from the feed of the posts.

> Every user can log out via the **"Logout"** button.
> The homepage of the application is also accessible on every page via the **"Home"** button.


### Used TECH to the app

BACKEND:

* **JDK** version is 1.8.0_221
* **IntelliJ IDEA**
* **SpringMVC** and **SpringBoot** framework
* **JPA** in the Persistence layer
* **Spring Security** to handle user registration and user roles
* **85%** tests coverage

UI:

* [Spring MVC Framework] with [Thymeleaf] template engine for generating the UI!
* [JavaScript] - many jQuerylots of ijax queries
* [HTML] - markup language for documents designed to be displayed in a web browser.
* [CSS] - style sheet language used for describing the presentation of a document written in.
* [Bootstrap] - great UI boilerplate for modern web apps

DATABASE:

* [SQL] - domain-specific language used in programming and designed for managing data held in a relational database management system (RDBMS)
* [MariaDB] - fork of the MySQL relational database management system (RDBMS).

API DOCUMENTATION:

* [Swagger] - give information for successfully consuming and integrating with an API.

ADDITIONAL:

* [GitLab] - distributed version-control system for tracking changes.
Link to App: https://gitlab.com/yavoryankov83/social_network_web_application
* [Trello] - a visual tool for organizing your work. 
Link to App Trello: https://trello.com/b/ApWxpQIi/social-network
* [Heroku] - deployment of the application code to a virtual machine on Heroku. 
Link to App: https://traveller-social-network.herokuapp.com

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [SQL]: <https://www.tutorialspoint.com/sql/index.htm>
   [HTML]: <https://www.tutorialspoint.com/html/index.htm>
   [CSS]: <https://getbootstrap.com/docs/3.4/css/>
   [GitLab]: <https://gitlab.com/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Bootstrap]: <https://getbootstrap.com/>
   [jQuery]: <http://jquery.com>
   [Trello]: <https://trello.com/>
   [Heroku]: <https://www.heroku.com/>
   [Swagger]: <https://swagger.io/>
   [Spring MVC Framework]: <https://spring.io/>
   [Thymeleaf]: <https://www.thymeleaf.org/>
   [MariaDB]: <https://mariadb.org/>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
