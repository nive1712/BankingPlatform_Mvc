<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en" xml:lang="en">
<head>
    <title>Block Details</title>
</head>
<body>
    <h1>Card Block Details</h1>
    <c:if test="${not empty error}">
        <p style="color: red;">${error}</p>
    </c:if>
    <c:if test="${not empty blockDetails}">
        <p><strong>Account Number:</strong> ${blockDetails.accountNumber}</p>
        <p><strong>Reason:</strong> ${blockDetails.reason}</p>
        <p><strong>Blocked At:</strong> ${blockDetails.blockedAt}</p>
    </c:if>
</body>
</html>
