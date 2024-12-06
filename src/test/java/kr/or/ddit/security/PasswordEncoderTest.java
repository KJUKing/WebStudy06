package kr.or.ddit.security;

import static org.junit.jupiter.api.Assertions.*;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import kr.or.ddit.annotation.RootContextWebConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RootContextWebConfig
class PasswordEncoderTest {

	@Inject
	PasswordEncoder encoder;
	
	@Test
	void test() {
		String plain = "java";
		String encrypted = encoder.encode(plain);
		log.info("encrypted : {}", encrypted);
	}

}











