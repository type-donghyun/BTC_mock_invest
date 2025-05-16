package kim.donghyun.auth.dto;

public class SigninRequest {
	private String email;
	private String password;

	// Getter/Setter
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
