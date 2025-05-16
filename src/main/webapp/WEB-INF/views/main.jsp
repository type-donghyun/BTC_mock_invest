<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>메인 페이지</title>
</head>
<body>
	<h1>비트코인 모의 투자 플랫폼</h1>

	<c:choose>
		<c:when test="${not empty user}">
			<p>
				<strong>${user.username}</strong>님, 환영합니다!
			</p>
			<a href="/logout">로그아웃</a>
		</c:when>
		<c:otherwise>
			<p>로그인하지 않은 상태입니다.</p>
			<a href="/signin">로그인</a>
		</c:otherwise>
	</c:choose>
</body>
</html>
