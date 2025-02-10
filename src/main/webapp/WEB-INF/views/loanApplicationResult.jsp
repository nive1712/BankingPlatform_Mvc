<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Loan Application Result</title>
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
            overflow: hidden;
        }
        .result-container {
            width: 400px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            box-sizing: border-box;
            position: relative;
            transition: transform 0.6s ease-in-out;
        }
        .result-container.swipe-left {
            transform: translateX(-100%);
        }
        h1 {
            margin-bottom: 20px;
            color: #333;
            text-align: center;
        }
        .details p {
            margin: 10px 0;
        }
        .message {
            text-align: center;
            margin-top: 20px;
        }
        .links {
            text-align: center;
            margin-top: 20px;
        }
        .links a {
            display: inline-block;
            margin: 10px;
            color: #007bff;
            text-decoration: none;
            border: 1px solid #007bff;
            border-radius: 6px;
            padding: 10px 20px;
            transition: background-color 0.3s, color 0.3s;
        }
        .links a:hover {
            background-color: #007bff;
            color: #fff;
        }
    </style>
    <script>
        function swipeLeft() {
            document.querySelector('.result-container').classList.add('swipe-left');
        }
        document.addEventListener('DOMContentLoaded', function() {
            swipeLeft(); // Trigger swipe effect on page load
        });
    </script>
</head>
<body>
    <div class="result-container">
        <h1>Loan Application Result</h1>
        <div class="details">
            <c:if test="${not empty loan}">
                <p>Loan ID: ${loan.loanId}</p>
                <p>Bank Account ID: ${loan.bankAccountId}</p>
                <p>Total Payment Amount: ${loan.totalPayment}</p>
                <p>Down Payment Amount: ${loan.downPayment}</p>
                <p>Tenure (Years): ${loan.tenureYears}</p>
                <p>Loan Type: ${loan.loanType}</p>
                <p>EMI Amount: ${loan.emiAmount}</p>
                <p>Sanction Date: ${loan.sanctionDate}</p>
            </c:if>
        </div>
        <div class="message">
            <c:if test="${not empty error}">
                <p style="color: red;">${error}</p>
            </c:if>
        </div>
        
            <!-- Back to Home link -->
    <div class="back-to-home">
        <a href="${pageContext.request.contextPath}/netbanking">Back to Home</a>
    </div>
        
    </div>
</body>
</html>
