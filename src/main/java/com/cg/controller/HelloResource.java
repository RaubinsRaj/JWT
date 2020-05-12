package com.cg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cg.entity.AuthenticationRequest;
import com.cg.entity.AuthenticationResponse;
import com.cg.service.MyUserDetailsService;
import com.cg.util.JwtUtil;



@RestController
public class HelloResource {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired 
	private JwtUtil jwtTokenUtil;
	
	@RequestMapping({"/hello"})
	public String hello() {
		return "HelloWorld";
	
	}
	
	@RequestMapping(value="/authenticate", method= RequestMethod.POST)
	public ResponseEntity<?> createAuthentcationToken(@RequestBody AuthenticationRequest authenticationRequest)throws Exception
	{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),authenticationRequest.getPassword())
					);		
		}
		catch(BadCredentialsException e){
			throw new Exception("incorrect username and password"+e);
		}
		
		final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		final String jwt=jwtTokenUtil.generateToken(userDetails);
	
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
		
	}
}
