package com.study.springSecurity.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.study.springSecurity.model.dto.SignRequestDTO;
import com.study.springSecurity.model.dto.SignResponseDTO;
import com.study.springSecurity.model.service.SignService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SignController {

	private final SignService signService;
	
	@PostMapping(value = "/register")
	public ResponseEntity<SignResponseDTO> signup(@RequestBody SignRequestDTO signRequest) throws Exception {
		return new ResponseEntity<>(signService.register(signRequest), HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<SignResponseDTO> signin(@RequestBody SignRequestDTO signRequest) throws Exception {
		return new ResponseEntity<>(signService.login(signRequest), HttpStatus.OK);
	}
	
	@GetMapping("/refresh")
	public ResponseEntity<SignResponseDTO> refresh(@RequestBody SignRequestDTO signRequest) throws Exception {
		// front에서 access 토큰이 만료되었을 시 refresh토큰을 확인 후 access토큰을 재발급해줌
		return new ResponseEntity<>(signService.refreshAccessToken(signRequest) , HttpStatus.OK);
	}
	
}
