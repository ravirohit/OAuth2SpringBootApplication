Ref link: 
  https://howtodoinjava.com/spring-boot2/oauth2-auth-server/
  https://dzone.com/articles/secure-spring-rest-api-using-oauth2-1
  https://stackoverflow.com/questions/46862840/unable-to-use-resourceserverconfigureradapter-and-websecurityconfigureradapter-i
customUserDetailsService impl ref link:
  http://progressivecoder.com/implementing-spring-boot-security-using-userdetailsservice/
-> Implementation to add custom token in Jwt token:
  https://www.baeldung.com/spring-security-oauth-jwt-legacy
-> Ref to skip Oauth user approval page to direct from login page to client redirect url with Auth Code.
  https://stackoverflow.com/questions/29696004/skip-oauth-user-approval-in-spring-boot-oauth2

---------------- Application Design for Oauth 2 implementation --------------
-> there are three software component to implement oAuth2:
   1. OAuth client: using Postman 
   2. Authorization Server: created a spring boot OAuth 2 server: "oauth2springbootJWTAuthServer"
   3. Resource server: create a spring boot OAuth 2 Resource server: "oauth2springbootJWTResourceServer"
-> Steps to test one complete flow of OAuth 2:
   1. start Authorization server, "oauth2springbootJWTAuthServer". it run on 8080 port.
   2. start Resource server, "oauth2springbootJWTResourceServer". it run on 8081 port.
-> Steps to get Auth code from Authorization server:
   - browse to link: 
     http://localhost:8080/oauth/authorize?client_id=clientapp&response_type=code&scope=read_profile_info
   - it will prompt a login screen:
     - provide any username
     - password is same what you have entered for username.
   - it provide you Authorization code in the redirect url:
     - assume it will redirect to url: http://localhost:8080/authcode?code=ee2hPN
     - Auth code is: ee2hPN
-> Steps to get access token out of Auth code:
   - make a post request from postman to url:
     http://localhost:8081/api/users/me
   - provide below two header:
     Content-Type: application/x-www-form-urlencoded
     Authorization: Basic Y2xpZW50YXBwOjEyMzQ1Ng==
     NOTE: authorization value is nothing but: 
      Authorization: Basic ${cliendId:secret}           // defined in OAuth2AuthorizationServer.java
   - for body: 
     - select: "x-www-form-urlencoded" radio option in postman and provide below key value pair:
       grant_type: authorization_code
       code: ee2hPN                                    // This value is nothing but Auth code got in previous steps.
       redirect_uri: http://localhost:8080/authcode   // ?????????????
   - now hit the request you will get a response with access token as below:
     {
	    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InJhdmkiLCJzY29wZSI6WyJyZWFkX3Byb2ZpbGVfaW5mbyJdLCJleHAiOjE2MTE1OTc4ODAsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiMWExN2FkZTMtNmZjZi00NjVhLTg4ODEtYWRlN2RmN2EzMjQxIiwiQ3VzdG9tIFBheWxvYWQiOiJyYXZpLWFiY2QiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.BDQQwRVzOtuFl0yhYqChKekBI1UVg1hdfyd6IZkGoLk",
	    "token_type": "bearer",
	    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InJhdmkiLCJzY29wZSI6WyJyZWFkX3Byb2ZpbGVfaW5mbyJdLCJhdGkiOiIxYTE3YWRlMy02ZmNmLTQ2NWEtODg4MS1hZGU3ZGY3YTMyNDEiLCJleHAiOjE2MTE1OTkwODAsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiNjExZGM4OTYtMjkyYS00MGIxLTg3MGEtNWZlYjUzZjI5ODhlIiwiQ3VzdG9tIFBheWxvYWQiOiJyYXZpLWFiY2QiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.gdHDmyerUdQx0Czyzo9jhXk4G68YPKq2dxV5aObX6wA",
	    "expires_in": 1199,
	    "scope": "read_profile_info",
	    "Custom Payload": "ravi-abcd",
	    "jti": "1a17ade3-6fcf-465a-8881-ade7df7a3241"
	  }
-> Now Access the protected api on the resource server using access token:
   GET api: http://localhost:8081/api/users/me
   Header:
     Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InJhdmkiLCJzY29wZSI6WyJyZWFkX3Byb2ZpbGVfaW5mbyJdLCJleHAiOjE2MTE1OTc4ODAsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiMWExN2FkZTMtNmZjZi00NjVhLTg4ODEtYWRlN2RmN2EzMjQxIiwiQ3VzdG9tIFBheWxvYWQiOiJyYXZpLWFiY2QiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.BDQQwRVzOtuFl0yhYqChKekBI1UVg1hdfyd6IZkGoLk
   NOTE: here we are providing the same access token what i have got in previous step.
   - Hit the api, you will get below response:
    {
	    "name": "ravi",
	    "email": "ravi@howtodoinjava.com"
	}
   - If token is wrong or expired. you will get below response:
     {
	    "error": "invalid_token",
	    "error_description": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsib2F1dGgyLXJlc291cmNlIl0sInVzZXJfbmFtZSI6InJhdmkiLCJzY29wZSI6WyJyZWFkX3Byb2ZpbGVfaW5mbyJdLCJleHAiOjE2MTE1OTc4ODAsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiMWExN2FkZTMtNmZjZi00NjVhLTg4ODEtYWRlN2RmN2EzMjQxIiwiQ3VzdG9tIFBheWxvYWQiOiJyYXZpLWFiY2QiLCJjbGllbnRfaWQiOiJjbGllbnRhcHAifQ.BDQQwRVzOtuFl0yhYqChKekBI1UVg1hdfyd6IZkGoLk"
	 }
=== ERROR ============
ERROR:
  o.s.s.c.bcrypt.BCryptPasswordEncoder     : Encoded password does not look like BCrypt
Solution:
  There is two places where user credential is used.
  1. when we are retrieving the user data from db and storing it in CustomUser class object. 
     this operation is happening in CustomUserDetailsService.java class.
  2. There is another place where we provide CustomUserDetailsService class object and PasswordEncoder object where 
     Spring boot would be reading user info from CustomUser object.
     this object is provided to spring boot in WebSecurityConfig.java file.
     NOTE: When Spring boot read password from CustomUser object. which has been initialized. it assume we have encoded the
     password first then initialize it. and when spring read the password, first it decode it before using.
  As i was not encoding the password before storing it in CustomUser object. and Spring was using the passowrd after 
  decoding, it was causing "Bad Credential" error in UI side and in backend error is "Encoded password does not look like BCrypt"
  Solution:
    I have added below code to encode the password before initialize it in CustomUserDetailsService.java:
    
    customUser.setPassword(passwordEncoder().encode(username));  and  // username is password string
    
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    }
    
===================== NOTE =============================
-> That we used a symmetric key in our JwtAccessTokenConverter to sign our tokens – which means we will need to use the 
   same exact key for the Resources Server as well.

   
   
   
   
   
   
   
   
   
   
   