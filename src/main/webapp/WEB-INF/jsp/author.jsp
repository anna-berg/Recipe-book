<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Author</title>
</head>
<body>
<h1>${requestScope.authorName}</h1>
<ul>
    <c:forEach var="recipe" items="${requestScope.recipes}">
        <li>
                ${recipe.categoryRecipeDto.category}: <a href="/recipe?recipeId=${recipe.id}">${recipe.title}</a>
        </li>
    </c:forEach>
</ul>
</body>
</html>
