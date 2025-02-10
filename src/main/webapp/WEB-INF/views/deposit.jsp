<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
    <meta charset="UTF-8">
    <title>Deposit</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #e0e0e0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            overflow: hidden;
        }
        .outer-container {
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(to right, #f0f0f0, #e0e0e0);
            padding: 20px; /* Added padding to ensure inner container doesn't touch the edges */
        }
        .inner-container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            animation: slideIn 0.8s ease-out forwards;
            box-sizing: border-box; /* Ensures padding is included in the element's total width and height */
        }
        @keyframes slideIn {
            from {
                transform: scale(0.8);
                opacity: 0;
            }
            to {
                transform: scale(1);
                opacity: 1;
            }
        }
        h1 {
            margin-bottom: 20px;
            color: #333;
            text-align: center;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
            color: #666;
        }
        .form-group input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 16px;
            box-sizing: border-box; /* Ensures padding is included in the element's total width */
        }
        .form-group button {
            padding: 12px 24px;
            border: none;
            border-radius: 6px;
            color: #fff;
            background-color: #007bff;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s, transform 0.3s;
            width: 100%;
            box-sizing: border-box; /* Ensures padding is included in the element's total width */
        }
        .form-group button:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }
        .form-group button:active {
            background-color: #004085;
            transform: translateY(0);
        }
    </style>
</head>
<body>
    <div class="outer-container">
        <div class="inner-container">
            <h1>Deposit</h1>
              <!-- Display error message if present -->
   
              <c:if test="${not empty error}">
        <div class="error">${error}</div>
             </c:if>
            <form action="${pageContext.request.contextPath}/netbanking/completedeposit" method="post">
                <div class="form-group">
                    <label for="accountNumber">Account Number:</label>
                    <input type="text" id="accountNumber" name="accountNumber" required>
                </div>
                <div class="form-group">
                    <label for="pin">PIN:</label>
                    <input type="password" id="pin" name="pin" required>
                </div>
                <div class="form-group">
                    <label for="amount">Amount:</label>
                    <input type="number" id="amount" name="amount" step="0.01" required>
                </div>
                <div class="form-group">
                    <button type="submit">Deposit</button>
                </div>
            </form>
             <!-- Back to Home link -->
             <div class="back-to-home">
                <a href="${pageContext.request.contextPath}/dashboard">Back to Home</a>
            </div>
        </div>
    </div>
</body>
</html>
