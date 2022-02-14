<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Category</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.categoryList}">
        <h1>Список категорий:</h1>
        <ul>
            <c:forEach var="category" items="${requestScope.categoryList}">
                <li>
                    <a href="/category?categoryId=${category.id}">${category.category}</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:when test="${not empty requestScope.categoryDto}">
        <h1> ${requestScope.categoryDto.category} </h1>
        <ul>
            <c:forEach var="recipe" items="${requestScope.recipeListByCategory}">
                <li>
                    <a href="/recipe?recipeId=${recipe.id}">${recipe.title}</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <h3>Нет такой категории</h3>
    </c:otherwise>
</c:choose>


</body>
</html>
