package com.study.springSecurity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.study.springSecurity.model.dto.SignRequestDTO;
import com.study.springSecurity.model.dto.SignResponseDTO;
import com.study.springSecurity.model.entity.Authority;
import com.study.springSecurity.model.entity.Member;
import com.study.springSecurity.model.repository.MemberRepository;
import com.study.springSecurity.model.service.SignService;
import com.study.springSecurity.security.enums.UserRole;
import com.study.springSecurity.security.provider.JwtProvider;

import jakarta.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
public class SignServiceTest {
	
	@Spy
	@InjectMocks
	private SignService target;
	
	@Mock
	private MemberRepository memberRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@Mock
	JwtProvider jwtProvider;

	SignRequestDTO request = new SignRequestDTO();
	
	@Test
	@DisplayName("login 테스트")
	public void login_test() throws Exception {
		// given
		Optional<Member> member = Optional.of(Member.builder()
				.id(1L)
				.account("test")
				.name("test")
				.email("test@abc.com")
				.roles(Collections.singletonList(Authority.builder().name(UserRole.USER.getValue()).build()))
				.build())
				;
		request.setAccount("test");
		
		doReturn(member).when(memberRepository).findByAccount(anyString());
		doReturn(true).when(passwordEncoder).matches(any(), any());
		doReturn("test").when(target).createRefreshToken(any());
		doReturn("test").when(jwtProvider).createToken(any(), any());
		
		// when
		final SignResponseDTO result = target.login(request);
		
		// then
		assertThat(result.getResult()).isEqualTo("success");
	}
}
