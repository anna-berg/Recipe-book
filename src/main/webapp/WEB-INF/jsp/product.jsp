<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Product</title>
</head>
<body>
<%@include file="header.jsp"%>

<c:choose>
    <c:when test="${not empty requestScope.productList}">
        <h1>Список всех продуктов: </h1>
        <ul>
            <c:forEach var="product" items="${requestScope.productList}">
                <li>
                    <a href="/product?productId=${product.id}">${product.name}</a>
                </li>
            </c:forEach>
        </ul>
    </c:when>
    <c:when test="${not empty requestScope.product}">
        <h1> ${requestScope.product.name} </h1>
        <p>Type: ${requestScope.product.type}</p>
        <p>Carbohydrates: ${requestScope.product.carbohydrates}</p>
        <p>Fats: ${requestScope.product.fats}</p>
        <p>Proteins: ${requestScope.product.proteins}</p>
    </c:when>
    <c:otherwise>
        <h3>Нет такого продукта</h3>
    </c:otherwise>
</c:choose>
</body>
</html>
