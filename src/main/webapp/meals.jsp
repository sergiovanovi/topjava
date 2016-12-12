<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>
<table border align="centre" border="1">
    <caption>Meals</caption>
    <tr>
        <th width="200">Date</th>
        <th width="200">Desc</th>
        <th width="100">Ccal</th>
    </tr>

    <c:forEach items="${list}" var="meal">

        <c:if test="${meal.exceed}">
        <tr bgcolor="red">
            <td width="200"><c:out value="${meal.dateTime}"/></td>
            <td width="200"><c:out value="${meal.description}"/></td>
            <td width="100"><c:out value="${meal.calories}"/></td>
        </tr>
        </c:if>
        <c:if test="${!meal.exceed}">
            <tr>
                <td width="200"><c:out value="${meal.dateTime}"/></td>
                <td width="200"><c:out value="${meal.description}"/></td>
                <td width="100"><c:out value="${meal.calories}"/></td>
            </tr>
        </c:if>
    </c:forEach>
</table>
</body>
</html>
