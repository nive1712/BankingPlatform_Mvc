<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
    <meta charset="UTF-8">
    <title>Budget Result</title>
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
            width: 400px;
            padding: 20px;
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
            text-align: center;
        }
        h2 {
            margin-bottom: 20px;
            color: #333;
        }
        p {
            margin: 10px 0;
            color: #666;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            border: none;
            border-radius: 6px;
            color: #fff;
            background-color: #007bff;
            cursor: pointer;
            font-size: 16px;
            text-decoration: none;
            transition: background-color 0.3s;
        }
        .button:hover {
            background-color: #0056b3;
        }
        canvas {
            margin-top: 20px;
        }
        .legend {
            margin-top: 20px;
            text-align: left;
        }
        .legend div {
            display: flex;
            align-items: center;
            margin-bottom: 5px;
        }
        .legend div span {
            display: inline-block;
            width: 20px;
            height: 20px;
            border-radius: 50%;
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Budget Result</h2>
        <p>Total Income: ${calculatedBudget.totalIncome}</p>
        <p>Total Expenses: ${calculatedBudget.totalExpenses}</p>
        <p>Debt Repayment: ${calculatedBudget.debtRepayment}</p>
        <p>Budget Balance: ${calculatedBudget.budgetBalance}</p>
        <a class="button" href="calculator">Go Back</a>

        <!-- Pie Chart -->
        <canvas id="pieChart" width="300" height="300"></canvas>

        <!-- Legend -->
        <div class="legend">
            <div><span style="background-color: #ff6384;"></span>Expenses</div>
            <div><span style="background-color: #36a2eb;"></span>Debt Repayment</div>
            <div><span style="background-color: #cc65fe;"></span>Balance</div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const ctx = document.getElementById('pieChart').getContext('2d');

                // Get the values from JSP and parse them into JavaScript variables
                const totalIncome = parseFloat('${calculatedBudget.totalIncome}');
                const totalExpenses = parseFloat('${calculatedBudget.totalExpenses}');
                const debtRepayment = parseFloat('${calculatedBudget.debtRepayment}');
                const budgetBalance = parseFloat('${calculatedBudget.budgetBalance}');

                const data = [
                    totalExpenses, 
                    debtRepayment,
                    budgetBalance
                ];
                const labels = ['Expenses', 'Debt Repayment', 'Balance'];
                const colors = ['#ff6384', '#36a2eb', '#cc65fe'];

                let total = data.reduce((acc, val) => acc + val, 0);
                let startAngle = 0;

                data.forEach((value, index) => {
                    let sliceAngle = (value / total) * 2 * Math.PI;
                    let endAngle = startAngle + sliceAngle;

                    ctx.beginPath();
                    ctx.moveTo(150, 150); // Center
                    ctx.arc(150, 150, 150, startAngle, endAngle);
                    ctx.closePath();
                    ctx.fillStyle = colors[index % colors.length];
                    ctx.fill();

                    startAngle = endAngle;
                });

                ctx.beginPath();
                ctx.arc(150, 150, 100, 0, 2 * Math.PI);
                ctx.closePath();
                ctx.lineWidth = 2;
                ctx.strokeStyle = '#ffffff';
                ctx.stroke();
            });
        </script>
    </div>
</body>
</html>
