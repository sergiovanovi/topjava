<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal list</title>
</head>
<body>
<h2><a href="index.html">Home</a></h2>

<div class="centered">
    <form method="POST" name="add">
        Id : <input type="text" readonly="readonly" name="id" value="<c:out value="${newMeal.id}"/>"/>
        Date : <input type="datetime-local" name="date" required="required" value="<c:out value="${newMeal.dateTime}"/>"/>
        Desc : <input type="text" name="desc" required="required" value="<c:out value="${newMeal.description}"/>"/>
        Ccal : <input type="number" min="0" name="ccal" required="required" value="<c:out value="${newMeal.calories}"/>"/>
        <input type="submit" value="Add/Update"/>
    </form>
</div>

<table border align="centre" border="1">
    <caption>Meals</caption>
    <tr bgcolor="#add8e6">
        <th width="50">Id</th>
        <th width="200">Date</th>
        <th width="200">Desc</th>
        <th width="100">Ccal</th>
        <th width="50">Remove</th>
        <th width="50">Edit</th>
    </tr>
    <c:forEach items="${list}" var="meal">
        <fmt:parseDate value="${meal.dateTime}" type="both" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate"/>
        <fmt:formatDate value="${parsedDate}" type="both" pattern="dd.MM.yyyy HH:mm" var="stdDatum"/>

        <c:if test="${meal.exceed}">
            <tr bgcolor="#f08080">
        </c:if>
        <c:if test="${!meal.exceed}">
            <tr bgcolor="#90ee90">
        </c:if>
                <td width="50"><c:out value="${meal.id}"/></td>
                <td width="200"><c:out value="${stdDatum}"/></td>
                <td width="200"><c:out value="${meal.description}"/></td>
                <td width="100"><c:out value="${meal.calories}"/></td>
                <td><a href="<c:url value="?action=remove&id=${meal.id}" />">Remove</a></td>
                <td><a href="<c:url value="?action=update&id=${meal.id}" />">Edit</a></td>
            </tr>
    </c:forEach>
</table>
</body>
</html>
