<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Block Card</title>
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
            width: 120px;
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
        .swipe-left {
            transform: translateX(-100%);
        }
    </style>
    <script>
        function swipeLeft() {
            document.querySelector('.form-container').classList.add('swipe-left');
        }
    </script>
</head>
<body>
    <div class="form-container">
        <h1>Block Card</h1>
        <form action="${pageContext.request.contextPath}/netbanking/blockCard" method="post" onsubmit="swipeLeft()">
            <div class="form-group">
                <label for="accountNumber">Account Number:</label>
                <input type="text" id="accountNumber" name="accountNumber" required />
            </div>
            <div class="form-group">
                <label for="pin">PIN:</label>
                <input type="password" id="pin" name="pin" required />
            </div>
            <div class="form-group">
                <label for="reason">Reason:</label>
                <input type="text" id="reason" name="reason" placeholder="Enter reason for blocking" required />
            </div>
            <button type="submit">Block Card</button>
        </form>
         <!-- Back to Home link -->
         <div class="back-to-home">
            <a href="${pageContext.request.contextPath}/dashboard">Back to Home</a>
        </div>
    </div>
</body>
</html>
