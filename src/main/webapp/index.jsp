<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Java Enterprise (Topjava)</title>
</head>
<body>
<h3>Проект <a href="https://github.com/JavaWebinar/topjava09" target="_blank">Java Enterprise (Topjava)</a></h3>
<hr>

<c:if test="${empty id}">
    <tr>Choose user</tr>
</c:if>
<c:if test="${!empty id}">
    <tr>Current user ${id}</tr>
</c:if>

<form method="post" action="index">
    <select name="userId">
        <option>1</option>
        <option>2</option>
    </select>
    <button type="submit">Login</button>
</form>

<ul>
    <li><a href="users">User List</a></li>
    <li><a href="meals">Meal List</a></li>
</ul>
</body>
</html>
