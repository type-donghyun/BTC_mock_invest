package kim.donghyun.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import kim.donghyun.auth.domain.User;
import kim.donghyun.auth.dto.SigninRequest;
import kim.donghyun.auth.dto.SignupRequest;
import kim.donghyun.auth.service.AuthService;

@Controller
public class AuthController {

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);
	@Autowired
	private AuthService authService;

	@GetMapping("/signin")
	public String signinForm() {
		return "signin";
	}

	@PostMapping("/signin")
	public String signin(@ModelAttribute SigninRequest request, HttpSession session, Model model) {
		try {
			User user = authService.Signin(request);
			session.setAttribute("user", user);
			return "redirect:/";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			return "signin";
		}
	}

	@GetMapping("/signup")
	public String signupForm() {
		return "signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute SignupRequest request, Model model) {
		try {
			authService.signup(request);
			return "redirect:/signin";
		} catch (RuntimeException e) {
			model.addAttribute("error", e.getMessage());
			return "signup";
		}
	}

	@GetMapping("/signout")
	public String signout(HttpSession session) {
		session.invalidate(); // 세션 무효화
		return "redirect:/signin";
	}

	@GetMapping("/")
	public String mainPage(HttpSession session, Model model) {
		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("user", loginUser); // 로그인 여부 관계없이 넘김(null 가능)
		return "main";
	}
}
