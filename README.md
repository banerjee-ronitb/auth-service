
<h1 align="left"> Social Networking App Demonstration </h1>

<h2> Description </h2>
This is an effort to demonstrate a social networking application that has users, followers, posts and feeds. This repository is built to handle user authentication and authorization related functionalities.

- For user followers, please refer to <a href="https://github.com/banerjee-ronitb/graph-service"> Graph Service </a>
- For user posts, please refer to <a href="https://github.com/banerjee-ronitb/post-service"> Post Service </a>
- For user feeds, please refer to <a href="https://github.com/banerjee-ronitb/newsfeed-service"> Newsfeed Service </a>

<h2> Authentication Microservice </h2>

This service showcases integration with <a href="https://www.okta.com/">OKTA</a> to sign up new users. Existing users can login to generate an access token.
 
<h3> Getting Started </h3>

Follow the steps below:

<h4> 🖐 Requirements </h4>

- Java 11
- Maven 3.6.x
- OKTA developer account

<h4> ⏳ Installation </h4>

This application uses maven to build. Refer to <a href="https://maven.apache.org/install.html"> Installing Maven </a> to install maven.

```bash
Step 1: mvn clean install
```
```bash
Step 2: mvn spring-boot:run
```

