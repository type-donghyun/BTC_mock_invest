package kim.donghyun.auth.service;

import kim.donghyun.auth.domain.User;
import kim.donghyun.auth.dto.SigninRequest;
import kim.donghyun.auth.dto.SignupRequest;
import kim.donghyun.auth.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public void signup(SignupRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("이미 존재하는 이메일입니다.");
		}

		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setUsername(request.getUsername());
		user.setCreatedAt(LocalDateTime.now());

		userRepository.save(user);
	}

	public User Signin(SigninRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("이메일이 존재하지 않습니다."));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		return user;
	}
}
