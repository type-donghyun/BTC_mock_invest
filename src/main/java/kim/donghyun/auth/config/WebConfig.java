package kim.donghyun.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginCheckInterceptor()).addPathPatterns("/**") // 모든 요청 가로채되
				.excludePathPatterns("/", "/signin", "/signup", "/logout", // 로그인 관련은 제외
						"/css/**", "/js/**", "/images/**" // 정적 리소스도 제외
				);
	}
}
