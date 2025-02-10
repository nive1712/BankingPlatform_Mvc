<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Loan Application</title>
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
        .form-container {
            width: 400px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            box-sizing: border-box;
            position: relative;
            transition: transform 0.6s ease-in-out;
        }
        .form-group {
            margin-bottom: 15px;
            display: flex;
            align-items: center;
        }
        .form-group label {
            width: 150px;
            color: #333;
        }
        .form-group input {
            flex: 1;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 16px;
        }
        button {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            color: #fff;
            background-color: #007bff;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s, transform 0.3s;
            width: 100%;
        }
        button:hover {
            background-color: #0056b3;
        }
        button:active {
            background-color: #004085;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>Apply for a Loan</h1>
        <form action="applyLoan" method="post">
            <div class="form-group">
                <label for="bankAccountId">Bank Account ID:</label>
                <input type="text" id="bankAccountId" name="bankAccountId" required>
            </div>
            <div class="form-group">
                <label for="totalPaymentAmount">Total Payment Amount:</label>
                <input type="number" step="0.01" id="totalPaymentAmount" name="totalPaymentAmount" required>
            </div>
            <div class="form-group">
                <label for="downPaymentAmount">Down Payment Amount:</label>
                <input type="number" step="0.01" id="downPaymentAmount" name="downPaymentAmount" required>
            </div>
            <div class="form-group">
                <label for="tenureYears">Tenure (Years):</label>
                <input type="number" id="tenureYears" name="tenureYears" required>
            </div>
            <div class="form-group">
                <label for="loanType">Loan Type:</label>
                <input type="text" id="loanType" name="loanType" required>
            </div>
            <button type="submit">Apply Loan</button>
            <c:if test="${not empty error}">
                <p style="color: red; text-align: center; margin-top: 15px;">${error}</p>
            </c:if>
            <c:if test="${not empty success}">
                <p style="color: green; text-align: center; margin-top: 15px;">${success}</p>
            </c:if>
        </form>
           <!-- Back to Home link -->
    <div class="back-to-home">
        <a href="${pageContext.request.contextPath}/netbanking">Back to Home</a>
    </div>
    </div>
</body>
</html>
