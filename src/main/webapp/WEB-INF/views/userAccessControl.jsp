<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Access Control</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }
        h1 {
            text-align: center;
            color: #333;
        }
        table {
            margin: 20px auto;
            border-collapse: collapse;
            width: 80%;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            background-color: #fff;
        }
        th, td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f4f4f4;
            text-transform: uppercase;
            letter-spacing: 0.1em;
        }
        tr:hover {
            background-color: #f1f1f1;
        }
        button {
            padding: 8px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s ease;
        }
        .approve-button {
            background-color: #28a745;
            color: white;
        }
        .approve-button:hover {
            background-color: #218838;
        }
        .reject-button {
            background-color: #dc3545;
            color: white;
        }
        .reject-button:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <h1>User Access Control</h1>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Request</th>
            <th>Admin Approval</th>
            <th>Action</th>
        </tr>
        <c:forEach var="status" items="${cardBlockStatusList}">
            <tr>
                <td>${status.id}</td>
                <td>${status.request}</td>
                <td>${status.adminApproval}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/approveCardBlock" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${status.id}">
                        <button type="submit" class="approve-button">Approve</button>
                    </form>
                    <form action="${pageContext.request.contextPath}/admin/rejectCardBlock" method="post" style="display:inline;">
                        <input type="hidden" name="id" value="${status.id}">
                        <button type="submit" class="reject-button">Reject</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
