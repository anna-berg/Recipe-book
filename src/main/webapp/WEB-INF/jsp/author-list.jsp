<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Authors</title>
</head>
<body>
<h1>Список авторов:</h1>
<ul>
    <c:forEach var="author" items="${requestScope.author}">
        <li>
            <a href="/author?authorId=${author.id()}">${author.name()}</a>
        </li>
    </c:forEach>
</ul>
</body>
</html>
