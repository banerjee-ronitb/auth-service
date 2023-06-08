
<h1 align="left"> Social Networking App Demonstration </h1>

<h2> Description/Details: </h2>
This is an effort to demonstrate a social networking application that has users, followers, posts and feeds. This repository is built to handle user authentication and authorization related functionalities.

- For user followers, please refer to <a href="https://github.com/banerjee-ronitb/graph-service"> Graph Service </a>
- For user posts, please refer to <a href="https://github.com/banerjee-ronitb/post-service"> Post Service </a>
- For user feeds, please refer to <a href="https://github.com/banerjee-ronitb/newsfeed-service"> Newsfeed Service </a>

<h2> Authentication Microservice </h2>

This service showcases integration with <a href="https://www.okta.com/">OKTA</a> and Apache Kafka.
 
- New users can signup. Existing users can login to generate the access token.
- On every new user sign up, an User Create event is triggered to Apache Kafka.
- The event consumed by the <a href="https://github.com/banerjee-ronitb/graph-service"> graph service </a>
- This implementation is an example of OKTA Authorization Code by Grant flow.

- For information on OKTA Account setup, visit <a href="https://developer.okta.com/docs/guides/implement-grant-type/authcode/main/"> OKTA Docs </a>

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
<h4> Roadmap </h4>

Add an additional repository to showcase deployment using Kuberenetes.

