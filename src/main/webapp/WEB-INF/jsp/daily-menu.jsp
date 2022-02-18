<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Daily Menu</title>
</head>
<body>
<%@include file="header.jsp"%>
<c:choose>
    <c:when test="${not empty requestScope.dailyMenuList}">
        <h1>Список рецептов: </h1>
        <ul>
            <c:forEach var="dailyMenu" items="${requestScope.dailyMenuList}">
                <li>
                    <a href="/daily-menu?dailyMenuId=${dailyMenu.id}">${dailyMenu.title}</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:when test="${not empty requestScope.dailyMenuDto}">
        <h1> ${requestScope.dailyMenuDto.title} </h1>
        <ul>
            <h1>${requestScope.dailyMenuDto.breakfast.title}</h1>
            <p>${requestScope.dailyMenuDto.breakfast.categoryRecipeDto.category}</p>
            <p>${requestScope.dailyMenuDto.breakfast.description}</p>
            <h3>Продукты: </h3>
            <c:forEach var="product" items="${requestScope.dailyMenuDto.breakfast.products}">
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
