package com.study.springSecurity2.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springSecurity2.model.dto.TokenDTO;
import com.study.springSecurity2.model.dto.UserResponseDTO;
import com.study.springSecurity2.model.entity.Authority;
import com.study.springSecurity2.model.service.UserService;
import com.study.springSecurity2.security.enums.UserRole;

import jakarta.servlet.ServletException;

@SpringBootTest
public class UserControllerTest {

	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	ObjectMapper objectMapper;
	
	UserResponseDTO response = new UserResponseDTO();
	
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
	@WithMockUser
	@DisplayName("유저정보 테스트")
	public void getUser_test() throws Exception{
		// given
		response = UserResponseDTO.builder()
				.id(1L)
				.account("test")
				.name("test")
				.email("test@abc.com")
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.result("success")
				.token(TokenDTO.builder()
						.access_token("access")
						.refresh_token("refresh")
						.build())
				.build();
		
		doReturn(response).when(userService).getMember(any());
		// when
		ResultActions actions = mockMvc.perform(get("/user/get?account=test"));
		
		// then
		actions
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(toJsonString(response)))
			.andExpect(content().string(containsString("success")))
			;
	}
}
