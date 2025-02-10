<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Block Card Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f4f4f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .details-container {
            width: 400px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            box-sizing: border-box;
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
            text-align: center;
        }
        p {
            margin: 10px 0;
        }
        a {
            display: inline-block;
            padding: 10px 20px;
            color: #007bff;
            text-decoration: none;
            border: 1px solid #007bff;
            border-radius: 6px;
            transition: background-color 0.3s, color 0.3s;
        }
        a:hover {
            background-color: #007bff;
            color: #fff;
        }
    </style>
</head>
<body>
    <div class="details-container">
        <h2>Block Card Details</h2>
        <c:if test="${cardBlockStatus != null}">
            <p>Account Number: ${cardBlockStatus.bankAccount.accountId}</p>
            <p>Reason: ${cardBlockStatus.blockReason}</p>
            <p>Status: ${cardBlockStatus.status}</p>
            <p>${message}</p>
        </c:if>
        <c:if test="${cardBlockStatus == null}">
            <p>${message}</p>
        </c:if>
        <div class="back-to-home">
            <a href="${pageContext.request.contextPath}/netbanking/block">Back to Home</a>
        </div>
        
       
    </div>
</body>
</html>
