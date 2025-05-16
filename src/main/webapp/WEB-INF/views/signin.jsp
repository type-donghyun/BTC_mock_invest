<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>로그인</title>
</head>
<body>
	<h2>로그인</h2>
	<form action="/signin" method="post">
		<label>Email: <input type="email" name="email" required />
		</label><br /> <br /> <label>Password: <input type="password"
			name="password" required />
		</label><br /> <br />
		<button type="submit">로그인</button>
	</form>

	<%-- 로그인 실패 시 오류 메시지 표시 --%>
	<%
	String error = request.getParameter("error");
	if (error != null) {
	%>
	<p style="color: red;">이메일 또는 비밀번호가 올바르지 않습니다.</p>
	<%
	}
	%>

	<p>
		계정이 없으신가요? <a href="/signup">회원가입</a>
	</p>
</body>
</html>
