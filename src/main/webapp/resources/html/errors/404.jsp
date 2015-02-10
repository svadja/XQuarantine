<%-- 
    Document   : 404
    Created on : Jan 21, 2015, 1:55:28 PM
    Author     : sasav
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>404</title>
        <link rel="icon"      type="image/png" href="resources/img/favicon.png">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/quarantine.css'/>">
    </head>
    <body>
        <div id="error-box">
            <div id="error-number">404</div>
            <div id="error-message">Page not found!</div>
            <div id="error-home">    
                <a href="<c:url value="/" />" >Go Home</a> 
            </div>
        </div>
    </body>
</html>
