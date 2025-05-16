<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <h2>회원가입</h2>
    <form action="/signup" method="post">
        <label>Email:
            <input type="email" name="email" required />
        </label><br/><br/>

        <label>Password:
            <input type="password" name="password" required />
        </label><br/><br/>

        <label>비밀번호 확인:
            <input type="password" name="confirmPassword" required />
        </label><br/><br/>

        <label>닉네임:
            <input type="text" name="username" required />
        </label><br/><br/>

        <button type="submit">회원가입</button>
    </form>

    <%-- 오류 메시지가 있으면 표시 --%>
    <%
        String error = request.getParameter("error");
        if (error != null) {
    %>
        <p style="color:red;"><%= error %></p>
    <%
        }
    %>

    <p>이미 계정이 있으신가요? <a href="/signin">로그인</a></p>
</body>
</html>
