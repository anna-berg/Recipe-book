<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Login</title>
</head>
<body>
<form action="/login" method="post">
    <label for="email">E-mail:
        <input type="text" name="email" id="email" value="${param.email}" required>
    </label><br>
    <label for="password">Password:
        <input type="password" name="password" id="password" required>
    </label><br>
    <button type="submit">
        Login
    </button>
    <a href="${pageContext.request.contextPath}/registration">
        <button type="button">Register</button>
    </a>
    <c:if test="${param.error != null}">
        <div style="color: darkred">
            <span>E-mail or password incorrect</span>
        </div>
    </c:if>
</form>
</body>
</html>
