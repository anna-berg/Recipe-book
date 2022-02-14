<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Registration</title>
</head>
<body>
<form action="/registration" method="post"></form>
<label for="nameId">Name:
    <input type="text" name="name" id="nameId">
</label><br>
<label for="emailId">E-mail:
    <input type="test" name="email" id="emailId">
</label><br>
<label for="passwordId">Password:
    <input type="password" name="password" id="passwordId">
</label><br>
<select name="role" id="roleId">
    <c:forEach var="role" items="${requestScope.roles}">
        <option value="${role}">${role}</option>
    </c:forEach>
</select><br>
<c:forEach var="gender" items="${requestScope.gender}">
    <input type="radio" name="gender" value="${gender}"> ${gender}
    <br>
</c:forEach>
<button type="submit">Send</button>
</body>
</html>
