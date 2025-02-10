<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ATM Options</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #e0e0e0; /* Light grey background */
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            overflow: hidden;
        }

        .container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
            align-items: center;
            width: 100%;
            max-width: 1200px;
            padding: 40px;
            background-color: #f4f4f4; /* Light grey for the outer container */
            border-radius: 12px;
            box-shadow: 0 0 25px rgba(0, 0, 0, 0.2);
            animation: slideUp 0.8s ease-out forwards;
            position: relative;
        }

        @keyframes slideUp {
            from {
                transform: translateY(100%);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .btn-container {
            flex: 1 1 calc(50% - 20px);
            display: flex;
            justify-content: center;
            align-items: center;
            max-width: calc(50% - 20px);
        }

        .btn {
            display: block;
            padding: 15px 30px;
            background-color: #007bff; /* Blue for inner buttons */
            color: #fff; /* White text */
            text-decoration: none;
            font-size: 18px;
            font-weight: bold;
            border-radius: 8px;
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
            transition: background-color 0.3s, transform 0.3s, box-shadow 0.3s;
            text-align: center;
            width: 100%;
            max-width: 320px;
            position: relative;
            overflow: hidden;
        }

        .btn::before {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 300%;
            height: 300%;
            background: rgba(255, 255, 255, 0.2);
            transition: transform 0.5s;
            transform: translate(-50%, -50%) scale(0);
            border-radius: 50%;
            z-index: 0;
        }

        .btn:hover::before {
            transform: translate(-50%, -50%) scale(1);
        }

        .btn:hover {
            background-color: #0056b3; /* Darker blue on hover */
            transform: translateY(-5px);
            box-shadow: 0 12px 24px rgba(0, 0, 0, 0.3);
        }

        .btn:active {
            background-color: #004494; /* Even darker blue when active */
            transform: translateY(1px);
            box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>

<body>
    <div class="container">
        <div class="btn-container">
            <a href="${pageContext.request.contextPath}/atm/deposit" class="btn">Deposit</a>
        </div>
        <div class="btn-container">
            <a href="${pageContext.request.contextPath}/atm/withdraw" class="btn">Withdraw</a>
        </div>
        <div class="btn-container">
            <a href="${pageContext.request.contextPath}/atm/transfer" class="btn">Transfer</a>
        </div>
        <div class="back-to-home">
            <a href="${pageContext.request.contextPath}/dashboard">Back to Home</a>
        </div>
      
    </div>
</body>

</html>
