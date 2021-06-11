package com.oriental.service.service;

import com.oriental.service.appconfig.URLConfiguration;
import com.oriental.service.exceptionhandler.ResourceNotFoundException;
import com.oriental.service.exceptionhandler.RestTemplateResponseErrorHandler;
import com.oriental.service.model.*;
import com.oriental.service.repository.RoleRepository;
import com.oriental.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Value("${authserver.clientid}")
    private String clientID;

    @Value("${authserver.clientsecret}")
    private String clientSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Bean
    public BCryptPasswordEncoder getbCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Booking> getUserBookingDetails(String username, String authorization) {
        User currentUser = userRepository.findByUsername(username);

        if (currentUser != null) {

            HttpEntity httpEntity = new HttpEntity<>(setAccessTokenHeader(authorization));

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<Booking[]> responseEntity = restTemplate.exchange(
                    URLConfiguration.GET_BOOKING_DETAILS + currentUser.getId(), HttpMethod.GET, httpEntity, Booking[].class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return Arrays.asList(responseEntity.getBody());
            }
        }
        return null;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmailAddress(email);
    }

    @Override
    public ResponseEntity<User> findUserById(Integer userId) throws ResourceNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found for this ID : " + userId));

        return ResponseEntity.ok().body(user);
    }

    @Override
    public APIResponse getCustomerID(String username) {
        User user = userRepository.findByUsername(username);

        if (user != null)
            return new APIResponse(200, "Successful!", String.valueOf(user.getId()));
        else
            return new APIResponse(400, "Unsuccessful!");
    }

    @Override
    public APIResponse createUser(User user) {
        User usernameValidation = userRepository.findByUsername(user.getUsername());
        User emailValidation = userRepository.findByEmailAddress(user.getEmailAddress());

        if (usernameValidation != null)
            return new APIResponse(400, "Username is Taken!");

        if (emailValidation != null)
            return new APIResponse(400, "Email is Taken!");

        if (usernameValidation == null && emailValidation == null) {
            Role userRole = roleRepository.findByName("ROLE_USER");

            List<Role> roleList = new ArrayList<>();
            roleList.add(userRole);

            /*User updateUser = new User();
            updateUser.setFirstName(user.getFirstName());
            updateUser.setLastName(user.getLastName());
            updateUser.setEmailAddress(user.getEmailAddress());
            updateUser.setUsername(user.getUsername());
            updateUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            updateUser.setPhoneNumber(user.getPhoneNumber());*/

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            /*updateUser.setRoles(roleList);
            updateUser.setDateJoined(getCurrentDateTime());
            updateUser.setStatus("A");
            updateUser.setEnabled(Boolean.TRUE);
            updateUser.setAccountNonExpired(Boolean.TRUE);
            updateUser.setCredentialsNonExpired(Boolean.TRUE);
            updateUser.setAccountNonLocked(Boolean.TRUE);*/

            user.setRoles(roleList);
            user.setDateJoined(getCurrentDateTime());
            user.setStatus("A");
            user.setEnabled(Boolean.TRUE);
            user.setAccountNonExpired(Boolean.TRUE);
            user.setCredentialsNonExpired(Boolean.TRUE);
            user.setAccountNonLocked(Boolean.TRUE);

            User createdUser = userRepository.save(user);

            if (createdUser != null)
                return new APIResponse(200, "Successful!");
            else
                return new APIResponse(400, "Unsuccessful!");
        }

        return new APIResponse(400, "Unknown!");
    }

    @Override
    public APIResponse createAdminUser(User user) {
        User usernameValidation = userRepository.findByUsername(user.getUsername());
        User emailValidation = userRepository.findByEmailAddress(user.getEmailAddress());

        if (usernameValidation != null)
            return new APIResponse(400, "Username is Taken!");

        if (emailValidation != null)
            return new APIResponse(400, "Email is Taken!");

        if (usernameValidation == null && emailValidation == null) {
            Role userRole = roleRepository.findByName("ROLE_ADMIN");

            List<Role> roleList = new ArrayList<>();
            roleList.add(userRole);

            /*User updateUser = new User();
            updateUser.setFirstName(user.getFirstName());
            updateUser.setLastName(user.getLastName());
            updateUser.setEmailAddress(user.getEmailAddress());
            updateUser.setUsername(user.getUsername());
            updateUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            updateUser.setPhoneNumber(user.getPhoneNumber());*/

            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            /*updateUser.setRoles(roleList);
            updateUser.setDateJoined(getCurrentDateTime());
            updateUser.setStatus("A");
            updateUser.setEnabled(Boolean.TRUE);
            updateUser.setAccountNonExpired(Boolean.TRUE);
            updateUser.setCredentialsNonExpired(Boolean.TRUE);
            updateUser.setAccountNonLocked(Boolean.TRUE);*/

            user.setRoles(roleList);
            user.setDateJoined(getCurrentDateTime());
            user.setStatus("A");
            user.setEnabled(Boolean.TRUE);
            user.setAccountNonExpired(Boolean.TRUE);
            user.setCredentialsNonExpired(Boolean.TRUE);
            user.setAccountNonLocked(Boolean.TRUE);

            User createdUser = userRepository.save(user);

            if (createdUser != null)
                return new APIResponse(200, "Successful!");
            else
                return new APIResponse(400, "Unsuccessful!");
        }

        return new APIResponse(400, "Unknown!");
    }

    @Override
    public APIResponse updateUser(String username, User user) {
        User currentUser = userRepository.findByUsername(username);

        if (currentUser != null) {
            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setEmailAddress(user.getEmailAddress());
            currentUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            currentUser.setPhoneNumber(user.getPhoneNumber());

            userRepository.save(currentUser);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "User Not Found!");
        }
    }

    @Override
    public APIResponse deleteUser(String username) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user != null) {
            user.setStatus("D");

            userRepository.save(user);

            return new APIResponse(200, "Successful!");
        } else {
            return new APIResponse(400, "Unsuccessful!");
        }
    }

    @Override
    public APIResponse userLogin(String username, String password) throws ResourceNotFoundException {
        User dbUser = userRepository.findByUsername(username);

        if (dbUser == null) {
            // throw new ResourceNotFoundException("Username or Password is Wrong!");
            return new APIResponse(400, "Username or Password is Wrong!");
        } else {
            if (!bCryptPasswordEncoder.matches(password, dbUser.getPassword())) {
                // throw new ResourceNotFoundException("Username or Password is Wrong!");
                return new APIResponse(400, "Username or Password is Wrong!");
            } else {

                String userRole;

                if (dbUser.getRoles().stream().allMatch(name -> name.getName().equals("ROLE_USER")))
                    userRole = "U";
                else
                    userRole = "A";

                // Call and get Access Token
                MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
                multiValueMap.add("username", username);
                multiValueMap.add("password", password);
                multiValueMap.add("grant_type", "password");

                HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, setHeaders());

                ResponseEntity<AccessToken> responseEntity = restTemplate.exchange(
                        URLConfiguration.GET_ACCESS_TOKEN, HttpMethod.POST, httpEntity, AccessToken.class);

                return new APIResponse(200, "Successful!", userRole, responseEntity.getBody());
            }
        }
    }

    @Override
    public APIResponse refreshAccessToken(String username, String refreshToken) {
        User dbUser = userRepository.findByUsername(username);

        if (dbUser == null) {
            return new APIResponse(400, "Username or Password is Wrong!");
        } else {
            // Refresh Access Token
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.add("grant_type", "refresh_token");
            multiValueMap.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(multiValueMap, setHeaders());

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<AccessToken> responseEntity = restTemplate.exchange(
                    URLConfiguration.GET_ACCESS_TOKEN, HttpMethod.POST, httpEntity, AccessToken.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return new APIResponse(200, "New Token!", responseEntity.getBody());
            } else {
                return new APIResponse(400, "Unsuccessful!");
            }
        }
    }

    @Override
    public APIResponse checkAccessToken(String username, String accessToken, String refreshToken) {
        User dbUser = userRepository.findByUsername(username);

        if (dbUser == null) {
            return new APIResponse(400, "Username or Password is Wrong!");
        } else {
            // Check Token
            HttpEntity httpEntity = new HttpEntity<>(setHeaders());

            restTemplate.setErrorHandler(restTemplateResponseErrorHandler);

            ResponseEntity<Object> responseEntity = restTemplate.exchange(
                    URLConfiguration.CHECK_ACCESS_TOKEN + accessToken, HttpMethod.GET, httpEntity, Object.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return new APIResponse(200, "Continue!");
            } else {
                return refreshAccessToken(username, refreshToken);
            }
        }
    }

    @Override
    public APIResponse forgotPassword(String emailAddress) {
        return null;
    }

    @Override
    public Long countAllUsers() {
        return userRepository.count();
    }

    private HttpHeaders setHeaders() {
        String clientCredentials = clientID + ":" + clientSecret;
        String authBasicHeaderClient = "Basic " + Base64.getEncoder().encodeToString(clientCredentials.getBytes());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, authBasicHeaderClient);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return httpHeaders;
    }

    private HttpHeaders setAccessTokenHeader(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, accessToken);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(TimeZone.getTimeZone("Asia/Kolkata").toZoneId());
    }
}
