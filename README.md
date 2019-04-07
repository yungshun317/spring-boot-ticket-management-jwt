# Spring Boot Ticket Management JWT

 [![Made with JAVA](https://img.shields.io/badge/Made%20with-JAVA-red.svg)](https://img.shields.io/badge/Made%20with-JAVA-red.svg) [![Spring Framework](https://img.shields.io/badge/Framework-Spring-orange.svg)](https://img.shields.io/badge/Framework-Spring-orange.svg) [![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) 

This project is modified from the example code of Packt's [Building RESTful Web Services with Spring 5 Second Edition](https://www.packtpub.com/application-development/building-restful-web-services-spring-5-second-edition) book. We have basic CRUD (Create, Read, Update, and Delete) APIs and the ability to generate JSON Web Tokens (JWTs), access and retrieve information from JWTs in our web service calls.

## Usage
### Prerequisites
* Install java, maven.
* (Optional) install IntelliJ.

### Run
Run `@SpringBootApplication public class SpringBootTicketManagementJwtApplication`.

## Features
### CRUD operations
We fill the basic endpoints for all CRUD operations in `@RestController public class UserController` and implement the APIs in `@Service public class UserServiceImpl implements UserService`. We have 2 GET methods (`getAllUsers` and `getUser`), 1 POST method (`createUser`), 1 PUT method (`updateUser`), and 1 DELETE method (`deleteUser`). More details are illustrated in this [spring-boot-ticket-management](https://github.com/yungshun317/spring-boot-ticket-management.git) document. For simplicity's sake, I remove the file uploading functionality form the previous project.

### JSON Web Token (JWT)
Spring Security can be applied in many forms, including XML configurations and using powerful libraries such as JWT.

JWT tokens are URL-safe and web browser-compatible especially for Single Sign-On (SSO) contexts. JWT has 3 parts:
 - Header
 - Payload
 - Signature
 
The header part decides which algorithm should be used to generate the token. While authenticating, the client has to save the JWT, which is returned by the server. Unlike traditional session creation approaches, this process doesn't need to store any cookies on the
client side. JWT authentication is stateless as the client state is never saved on a server.

We define the endpoints for generating tokens and getting subjects in `HomeController`.
```
@ResponseBody
@RequestMapping("/security/generate/token")
public Map<String, Object> generateToken(@RequestParam(value="subject") String subject) {
    String token = securityService.createToken(subject, (15 * 1000 * 60));

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("result", token);

    return map;
}

@ResponseBody
@RequestMapping("/security/get/subject")
public Map<String, Object> getSubject(@RequestParam(value="token") String token) {
    String subject = securityService.getSubject(token);

    Map<String, Object> map = new LinkedHashMap<>();
    map.put("result", subject);

    return map;
}
```
In `@Service public class SecurityServiceImpl implements SecurityService`, we implement `createToken` and `getSubject` methods.
```
@Override
public String createToken(String subject, long ttlMillis) {
    if (ttlMillis <= 0) {
        throw new RuntimeException("Expiry time must be greater than Zero :[" + ttlMillis + "] ");
    }

    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // The JWT signature algorithm we will be using to sign the token
    long nowMillis = System.currentTimeMillis();

    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

    JwtBuilder builder = Jwts.builder()
        .setSubject(subject)
        .signWith(signatureAlgorithm, signingKey);

    builder.setExpiration(new Date(nowMillis + ttlMillis));

    return builder.compact();
}

@Override
public String getSubject(String token) {
    Claims claims = Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
}
```
How to restrict web service calls by adding JWT security is discussed in my another project, [spring-boot-ticket-management-aop](https://github.com/yungshun317/spring-boot-ticket-management-aop).

### Generate a token
```sh
$ curl http://localhost:8080/security/generate/token?subject=one

{"result":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmUiLCJleHAiOjE1NDk4NjY0OTB9.uZsdPcefGJICGJo5Jbv0lXHbDRCZA3vWDPBp-op-5wM"}
```
### Get a subject from a JWT token
```sh
$ curl http://localhost:8080/security/get/subject?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmUiLCJleHAiOjE1NDk4NjY0OTB9.uZsdPcefGJICGJo5Jbv0lXHbDRCZA3vWDPBp-op-5wM

{"result":"one"}
```
## Tech
The tech stack I use in this project:
* [IntelliJ](https://www.jetbrains.com/idea/) - a Java integrated development environment (IDE) for developing computer software developed by JetBrains.
* [Spring Boot](http://spring.io/projects/spring-boot) - a new way to create Spring applications with ease.
* [jjwt](https://github.com/jwtk/jjwt) - JSON Web Token for Java and Android.

## Todos
 - Unit tests and functional tests.
 - Take reference from this [REST: Good Practices for API Design](https://medium.com/hashmapinc/rest-good-practices-for-api-design-881439796dc9) post.
 - Spring Cloud.

## License
[Spring Boot Ticket Management JWT](https://github.com/yungshun317/spring-boot-ticket-management-jwt) is released under the [MIT License](https://opensource.org/licenses/MIT) by [yungshun317](https://github.com/yungshun317).
