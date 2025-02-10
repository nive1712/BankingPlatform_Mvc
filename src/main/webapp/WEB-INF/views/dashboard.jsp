<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>
    <style>
        /* styles.css */

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #e0e0e0;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            overflow: hidden;
        }

        .container {
            width: 80%;
            max-width: 900px;
            background: #333;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.3);
            color: #fff;
            text-align: center;
            transform: scale(0.95);
            animation: fadeIn 0.5s forwards;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: scale(0.95);
            }
            to {
                opacity: 1;
                transform: scale(1);
            }
        }

        h1 {
            font-size: 36px;
            color: #f5f5f5;
            margin-bottom: 25px;
        }

        p {
            font-size: 20px;
            color: #d1d1d1;
            margin-bottom: 35px;
        }

        .button-container {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .button-container form {
            margin: 0;
        }

        button {
            background-color: #007bff;
            color: #fff;
            padding: 15px 30px;
            border: none;
            border-radius: 6px;
            font-size: 18px;
            font-weight: bold;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.3s, box-shadow 0.3s;
        }

        button:hover {
            background-color: #0056b3;
            transform: translateY(-3px);
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        button:active {
            background-color: #004494;
            transform: translateY(1px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>

<body>
    <div class="container">
        <h1>Welcome to the Dashboard</h1>
        <p>User email: ${email}</p>
        <div class="button-container">
            <form action="${pageContext.request.contextPath}/toNetBanking" method="post">
                <button type="submit">Go to NetBanking</button>
            </form>
            <form action="${pageContext.request.contextPath}/toAtm" method="post">
                <button type="submit">Go to ATM</button>
            </form>
            <form action="${pageContext.request.contextPath}/admin/userAccessControl" method="get">
                <button type="submit">Admin Control</button>
            </form>
        </div>
    </div>
</body>

</html>
