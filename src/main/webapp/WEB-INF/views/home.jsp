%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <style>
        /* Add your styles here */
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to Our Platform</h1>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Login</a>
        <form action="/register" method="get">
            <button type="submit">Register</button>
        </form>
    </div>
</body>
</html>
