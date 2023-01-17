package com.study.springSecurity2.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springSecurity2.model.dto.SignRequestDTO;
import com.study.springSecurity2.model.dto.SignResponseDTO;
import com.study.springSecurity2.model.dto.TokenDTO;
import com.study.springSecurity2.model.entity.Authority;
import com.study.springSecurity2.model.service.SignService;
import com.study.springSecurity2.security.enums.UserRole;

import jakarta.servlet.ServletException;

@SpringBootTest
public class SignControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private SignService signService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	SignController signController;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext context;
	
	SignRequestDTO request = new SignRequestDTO();
	
	SignResponseDTO response = new SignResponseDTO();
	
	@BeforeEach
	void setUp() throws ServletException {
		// security filter 추가
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(springSecurity())
				.build();
		
		
		
	}
	
	public String toJsonString(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
	
	@Test
	@DisplayName("회원가입 테스트")
	public void register_test() throws Exception{
		// given
		request = SignRequestDTO.builder()
				.account("mockTest")
				.password("test")
				.nickname("testNick")
				.email("test222@test.com")
				.build()
				;
		
		response = SignResponseDTO.builder()
				.result("success")
				.build();
		
		doReturn(response).when(signService).register(any());
		
		// when
		ResultActions actions = mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(request)));
		
		// then
		actions
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().string(toJsonString(response)))
			.andExpect(content().string(containsString("success")))
			;
	}
	
	@Test
	@DisplayName("로그인 테스트")
	public void login_test() throws Exception{
		// given
		request = SignRequestDTO.builder()
				.account("mockTest")
				.password("test")
				.nickname("testNick")
				.email("test222@test.com")
				.build()
				;
		
		response = SignResponseDTO.builder()
				.id(request.getId())
				.account(request.getAccount())
				.name(request.getName())
				.email(request.getEmail())
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.result("success")
				.token(TokenDTO.builder()
						.access_token("access")
						.refresh_token("refresh")
						.build())
				.build();
		
		doReturn(response).when(signService).login(any());
		
		// when
		ResultActions actions = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(request)));
		
		// then
		actions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(toJsonString(response)))
			.andExpect(content().string(containsString("success")))
			;
		
	}
	
	@Test
	@DisplayName("refresh token 재발급 테스트")
	public void refresh_test() throws Exception{
		// given
		request = SignRequestDTO.builder()
				.access_token("access")
				.refresh_token("refresh")
				.build()
				;
		
		response = SignResponseDTO.builder()
				.token(TokenDTO.builder()
						.access_token("access")
						.refresh_token("refresh")
						.build())
				.result("success")
				.build();
		
		doReturn(response).when(signService).refreshAccessToken(any());
		// when
		ResultActions actions = mockMvc.perform(get("/refresh")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJsonString(request)));
		
		// then
		actions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(toJsonString(response)))
			.andExpect(content().string(containsString("success")))
			;
	}
}
