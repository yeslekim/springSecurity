package com.study.springSecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springSecurity.model.dto.UserResponseDTO;
import com.study.springSecurity.model.service.UserService;
import com.study.springSecurity.security.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;

	@GetMapping("/get")
	public ResponseEntity<UserResponseDTO> getUser() throws Exception {
		return new ResponseEntity<>(userService.getMember(SecurityUtil.getAccount()), HttpStatus.OK);
	}
}
