package com.watermelon.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.jwt.JwtTokenProvider;
import com.watermelon.model.entity.Role;
import com.watermelon.model.entity.User;
import com.watermelon.model.response.EStatus;
import com.watermelon.model.response.ResponseData;
import com.watermelon.payload.request.LoginRequest;
import com.watermelon.payload.request.SignupRequest;
import com.watermelon.payload.response.JwtResponse;
import com.watermelon.payload.response.MessageResponse;
import com.watermelon.repository.UserRepository;
import com.watermelon.security.CustomUserDetailService;
import com.watermelon.security.CustomUserDetails;
import com.watermelon.service.RoleService;
import com.watermelon.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(value = "*")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenProvider tokenProvider;

	@GetMapping("/{id}")
	public ResponseEntity<ResponseData> getUser(@PathVariable long id) {
		Optional<User> user = userRepository.findById(id);

		return user.isPresent()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseData(user, EStatus.SUCCESS.getStatus(), EStatus.SUCCESS.getTitle(),EStatus.SUCCESS.getDescription()))
				: ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new ResponseData(null, EStatus.NOT_FOUND.getStatus(), EStatus.NOT_FOUND.getTitle(),EStatus.NOT_FOUND.getDescription()));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
		if (userService.existsByUsername(signupRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: this username is already!"));
		}
		if (userService.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: this email is already!"));
		}

		User user = new User();
		user.setUsername(signupRequest.getUsername());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		user.setEmail(signupRequest.getEmail());
		user.setPhone(signupRequest.getPhone());
		user.setStatus(signupRequest.isStatus());
		Set<String> strRoles = signupRequest.getListRoles();
		Set<Role> listRoles = new HashSet<>();

		if (strRoles.isEmpty()) {
			Role role = roleService.findByNameRole("ROLE_USER")
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			listRoles.add(role);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleService.findByNameRole("ROLE_ADMIN")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					listRoles.add(adminRole);
				case "user":
					Role userRole = roleService.findByNameRole("ROLE_USER")
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					listRoles.add(userRole);
				}
			});
		}
		user.setListRoles(listRoles);
		userService.saveOrUpdate(user);

		return ResponseEntity.badRequest().body(new MessageResponse("Sign up successfully!"));

	}

	@PostMapping("/signin")
	public ResponseEntity<?> signinUser(@RequestBody LoginRequest loginRequest) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					loginRequest.getUsername(), loginRequest.getPassword());
			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//			CustomUserDetails customUserDetails =  (CustomUserDetails) customUserDetailService.loadUserByUsername(loginRequest.getUsername());

			String jwt = tokenProvider.generateToken(customUserDetails);

			// lay cac quyen cua user
			List<String> listRoles = customUserDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok()
					.body(new JwtResponse(jwt, customUserDetails.getId(), customUserDetails.getUsername(),
							customUserDetails.getEmail(), customUserDetails.getPhone(), listRoles));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ResponseData(null, EStatus.UNAUTHORIZED.getStatus(), EStatus.UNAUTHORIZED.getTitle(),"username or password is invalid")
					);
		}
	}

	

}
