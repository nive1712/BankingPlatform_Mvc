<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">

<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <style>
        /* styles.css */

        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            text-align: center;
        }

        .login-container {
            width: 100%;
            max-width: 500px;
            margin: 0 auto;
        }

        .login-header {
            margin-bottom: 20px;
        }

        .login-header h1 {
            font-size: 28px;
            color: #333;
            margin: 0;
        }

        .login-header p {
            font-size: 18px;
            color: #666;
        }

        .login-form {
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
            color: #333;
        }

        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .btn {
            display: inline-block;
            padding: 10px 15px;
            color: #fff;
            text-align: center;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .btn-black {
            background-color: #333;
        }

        .btn-register {
            background-color: #007bff;
            margin-top: 10px;
            text-align: center;
            display: block;
        }

        .alert {
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div class="login-container">
        <c:if test="${not empty registrationSuccess}">
            <div class="alert alert-success" role="alert">${registrationSuccess}</div>
        </c:if>
        <c:if test="${error == 'true'}">
            <div class="alert alert-danger" role="alert">Wrong email or password</div>
        </c:if>
        <div class="login-header">
            <h1>Bank Application Login</h1>
            <p>Enter your credentials:</p>
        </div>
        <div class="login-form">
            <form action="${pageContext.request.contextPath}/process_login" method="post">
                <div class="form-group">
                    <label for="email">Email id:</label>
                    <input type="text" id="email" name="email"/>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password"/>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-black">Login</button>
                </div>
                <div class="form-group">
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-register">Don't have an account? Sign up</a>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
