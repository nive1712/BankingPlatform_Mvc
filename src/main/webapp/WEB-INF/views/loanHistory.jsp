
<!-- <!DOCTYPE html>-->
<!--<html lang="en" xml:lang="en">-->
<head>
    <title>Loan History</title>
</head>
<body>
    <h1>Loan History</h1>
    
    <c:if test="${not empty loanHistory}">
        <table border="1">
            <thead>
                <tr>
                    <th>Loan ID</th>
                    <th>Bank Account ID</th>
                    <th>Total Payment Amount</th>
                    <th>Down Payment Amount</th>
                    <th>Tenure (Years)</th>
                    <th>Loan Type</th>
                    <th>EMI Amount</th>
                    <th>Sanction Date</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="loan" items="${loanHistory}">
                    <tr>
                        <td>${loan.loanId}</td>
                        <td>${loan.bankAccountId}</td>
                        <td>${loan.totalPayment}</td>
                        <td>${loan.downPayment}</td>
                        <td>${loan.tenureYears}</td>
                        <td>${loan.loanType}</td>
                        <td>${loan.emiAmount}</td>
                        <td>${loan.sanctionDate}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
    
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    
    <a href="loanApplication">Apply for a loan</a>
       <!-- Back to Home link -->
       <div class="back-to-home">
        <a href="${pageContext.request.contextPath}/netbanking">Back to Home</a>
    </div>
</body>
</html>
