<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Recipe</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.recipeList}">
        <h1>Список рецептов: </h1>
        <ul>
            <c:forEach var="recipe" items="${requestScope.recipeList}">
                <li>
                    <a href="/recipe?recipeId=${recipe.id}">${recipe.title}</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:when test="${not empty requestScope.recipe}">
        <h1> ${requestScope.recipe.title} </h1>
        <ul>
            <p>${requestScope.recipe.categoryRecipeDto.category} ${requestScope.recipe.authorDto.name()}</p>
            <p>${requestScope.recipe.description}</p>
            <h3>Продукты: </h3>
            <c:forEach var="product" items="${requestScope.productList}">
                <li>
                        ${product.name}
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:otherwise>
        <h3>Нет такого рецепта</h3>
    </c:otherwise>
</c:choose>
</body>
</html>
