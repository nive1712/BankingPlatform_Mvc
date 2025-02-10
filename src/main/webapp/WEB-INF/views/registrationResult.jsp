<!-- registrationResult.jsp -->
<!--<!DOCTYPE html>
<html lang="en">-->

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Result</title>
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
        }

        .container {
            width: 100%;
            max-width: 500px;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 15px rgba(0, 0, 0, 0.1);
            text-align: center;
            opacity: 0;
            transform: translateY(30px);
            animation: slideUp 0.8s forwards;
        }

        h1 {
            font-size: 24px;
            color: #333;
            margin-bottom: 20px;
        }

        p {
            font-size: 18px;
            color: #333;
            margin-bottom: 20px;
        }

        a {
            display: inline-block;
            padding: 10px 20px;
            color: #fff;
            background-color: #007bff;
            text-decoration: none;
            border-radius: 4px;
            font-weight: bold;
            transition: background-color 0.3s;
        }

        a:hover {
            background-color: #0056b3;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
    </style>
</head>

<body>
    <div class="container">
        <h1>${message}</h1>
        <c:if test="${not empty accountNumber}">
            <p>Your bank account has been created with account number: ${accountNumber}</p>
        </c:if>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-login">Go back to Sign up?</a>
    </div>
</body>

</html>
