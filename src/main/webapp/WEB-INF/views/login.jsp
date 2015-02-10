<%-- 
    Document   : login
    Created on : Jan 20, 2015, 9:23:57 AM
    Author     : sasav
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login Page</title>
        <link rel="icon" type="image/png" href="<c:url value='/resources/img/favicon.png'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/quarantine.css'/>">
    </head>
    <body onload='document.loginForm.username.focus();'>
        <div id="login-box">
            <div id="loginHeader">Quarantine Login</div>
		<c:if test="${not empty error}">
			<div class="error">${error}</div>
		</c:if>
		<c:if test="${not empty msg}">
			<div class="msg">${msg}</div>
		</c:if>
            <form name='loginForm'
                  action="<c:url value='j_spring_security_check' />" method='POST'>
                <table>
                    <tr>
                        <td>
                            Email:
                        </td>
                        <td>
                            <input class="login_input" type='text' name='username' value=''>
                        </td>   
                    </tr>    
                    <tr>
                        <td>
                            Password:
                        </td>
                        <td >
                            <input class="login_input" type='password' name='password' />
                        </td>  
                    <tr>    
                    <tr>
                        <td>
                            <input name="submit" type="submit"	value="submit" />
                        </td>
                        <td>
                        </td>  
                    <tr>    
                </table>
            </form>
        </div>    
    </body>
</html>
