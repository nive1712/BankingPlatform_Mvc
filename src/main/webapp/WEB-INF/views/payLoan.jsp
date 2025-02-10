
<head>
    <title>Pay Loan</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .form-container {
            width: 100%;
            max-width: 600px;
            margin: 20px;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
            background-color: #ffffff;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        h2 {
            margin-top: 0;
            color: #333;
            font-size: 24px;
        }
        table {
            width: 100%;
            margin-bottom: 20px;
            border-collapse: collapse;
        }
        table td {
            padding: 12px;
            border-bottom: 1px solid #eee;
            color: #555;
        }
        table td:first-child {
            font-weight: bold;
            color: #333;
        }
        .error {
            color: #e74c3c;
            margin-bottom: 15px;
        }
        .success {
            color: #2ecc71;
            margin-bottom: 15px;
        }
        form {
            display: flex;
            justify-content: flex-end;
        }
        form input[type="submit"] {
            padding: 12px 24px;
            border: none;
            border-radius: 5px;
            background-color: #3498db;
            color: white;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        form input[type="submit"]:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2>Pay Loan</h2>

    <!-- Display success or error messages -->
    <c:if test="${not empty success}">
        <p class="success">${success}</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

    <!-- Display loan and bank account details -->
    <table>
        <tr>
            <td>Account Number:</td>
            <td>${bankAccount.accountNumber}</td>
        </tr>
        <tr>
            <td>EMI Amount:</td>
            <td>${loan.emiAmount}</td>
        </tr>
        <tr>
            <td>Tenure:</td>
            <td>${loan.tenureYears} years</td>
        </tr>
        <tr>
            <td>EMIs Paid:</td>
            <td>${loan.paidInMonths}/${loan.payableMonth}</td>
        </tr>
        <tr>
            <td>Total Loan Amount:</td>
            <td>${loan.totalPayment}</td>
        </tr>
        <tr>
            <td>Current Balance:</td>
            <td>${bankAccount.balance}</td>
        </tr>
    </table>

    <!-- Pay Loan Form -->
    <form action="${pageContext.request.contextPath}/netbanking/payLoan" method="post">
        <input type="hidden" name="loanId" value="${loan.loanId}" />
        <input type="submit" value="Pay EMI" />
    </form>
    <div class="back-to-home">
        <a href="${pageContext.request.contextPath}/netbanking">Back to Home</a>
    </div>
</div>

</body>
</html>
