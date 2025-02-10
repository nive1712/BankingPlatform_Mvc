<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Budget Calculator</title>
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
        .container {
            width: 100%;
            max-width: 400px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            box-sizing: border-box; /* Ensure padding is included in total width/height */
        }
        h2 {
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
            box-sizing: border-box; /* Ensure padding is included in width */
        }
        .form-group button {
            padding: 12px;
            border: none;
            border-radius: 6px;
            color: #fff;
            background-color: #007bff;
            cursor: pointer;
            font-size: 16px;
            transition: background-color 0.3s, transform 0.3s;
            width: 100%;
            box-sizing: border-box; /* Ensure padding is included in width */
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
    <div class="container">
        <h2>Budget Calculator</h2>
        <form:form modelAttribute="budget" action="calculate" method="post">
            <div class="form-group">
                <form:label path="totalIncome">Total Income:</form:label>
                <form:input path="totalIncome" />
            </div>
            <div class="form-group">
                <form:label path="totalExpenses">Total Expenses:</form:label>
                <form:input path="totalExpenses" />
            </div>
            <div class="form-group">
                <form:label path="debtRepayment">Debt Repayment:</form:label>
                <form:input path="debtRepayment" />
            </div>
            <div class="form-group">
                <button type="submit">Calculate</button>
            </div>
        </form:form>
        <div class="back-to-home">
            <a href="${pageContext.request.contextPath}/netbanking">Back to Home</a>
        </div>
    </div>
</body>
</html>
