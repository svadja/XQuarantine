<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>Quarantine</title>
        <link rel="icon"       type="image/png" href="<c:url value='/resources/img/favicon.png'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/quarantine.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/jquery.datetimepicker.css'/>">
        <script src="<c:url value='/resources/js/jquery.js'/>"></script>
        <script src="<c:url value='/resources/js/jquery.datetimepicker.js'/>"></script>
        <jsp:useBean id="dateValue" class="java.util.Date"/>
    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="welcome">
                    <c:if test="${pageContext.request.userPrincipal.name != null}">
                        Welcome : ${pageContext.request.userPrincipal.name} |  <a href="<c:url value="j_spring_security_logout" />" > Logout</a>
                    </c:if>
                </div>    
                <div id="logo">
                    <img alt="Quarantine" src="resources/img/logo.png">
                </div>
            </div>

            <form method="post" id="search">
                <table>

                    <!-- RECIPIENT -->
                    <td class="td_label">
                        <label>Recipient: </label>
                    </td>
                    <td class="td_rightborder">
                        <input class="emailfield" name="recipient" value="${recipient}" disabled="true"/>
                    </td>
                    <!-- SENDER -->
                    <td class="td_label">
                        <label>Sender: </label>
                    </td>
                    <td class="td_rightborder">
                        <input class="emailfield" name="sender"/>
                    </td>
                    <!-- DATE -->
                    <td class="td_leftpadding">
                        <label> From </label>
                    </td>
                    <td >
                        <input id="id_datestart" name="datestart" value="${datestart}" type="text"/>
                    </td>
                    <td >
                        <label> To </label>
                    </td>
                    <td class="td_rightborder">
                        <input id="id_dateend" name="dateend" value="${dateend}" type="text"/>
                    </td>
                    <!-- STATUS -->
                    <td class="td_leftpadding">
                        <label>Status </label>
                    </td>
                    <td class="td_rightborder">
                        <select name="status">
                            <option selected value="">ALL</option>
                            <option value="CLEAN">CLEAN</option>
                            <option value="SPAM">SPAM</option>
                            <option value="BLOCKED">BLOCKED</option>
                            <option value="VIRUS">VIRUS</option>
                        </select>
                    </td>
                    <!-- GO -->
                    <td id="td_search">
                        <input type="submit" value="Search">
                    </td>

                </table>
            </form>
            <div id="content_area">
                <table id="content_table">
                    <tr id="title_data" >
                        <td id="td_date">
                            Date
                        </td>
                        <td  id="td_sender">
                            Sender
                        </td>
                        <td  id="td_recipient">
                            Recipient
                        </td>
                        <td  id="td_subject">
                            Subject
                        </td>
                        <td id="td_size">
                            Size
                        </td>
                        <td id="td_status">
                            Status
                        </td>
                        <td id="td_action">
                            Action
                        </td>
                    </tr>
                    <c:forEach var="mail" items="${maillist}">
                        <tr class="trItems" >
                            <td>
                                <div class="text_date">
                                    <jsp:setProperty name="dateValue" property="time" value="${mail.timeMS}"/>
                                    <fmt:formatDate value="${dateValue}" pattern="yyyy/MM/dd HH:mm"/>
                                </div>
                            </td>
                            <td>
                                <div class="text_from">
                                    ${mail.sender}
                                </div>
                            </td>
                            <td>
                                <div class="text_to">
                                    ${mail.recipient}
                                </div>
                            </td>
                            <td >
                                <div class="text_subject">
                                    ${mail.subject}   
                                </div>

                            </td>
                            <td>
                                <div class="text_size">
                                    ${mail.sizeMsg}  
                                </div>
                            </td>
                            <td>
                                <c:set var="visibleBttnResend" value="true" />
                                <c:forEach var="status" items="${mail.status}">
                                    ${status}
                                    <c:if test="${status=='CLEAN'}">
                                        <c:if test="${fn:substring(accessResendUser.access, 0, 1)!='Y'}">
                                            <c:set var="visibleBttnResend" value="false" /> 
                                        </c:if>  
                                    </c:if>
                                    <c:if test="${status=='SPAM'}">
                                        <c:if test="${fn:substring(accessResendUser.access, 1, 2)!='Y'}">
                                            <c:set var="visibleBttnResend" value="false" /> 
                                        </c:if>       
                                    </c:if>
                                    <c:if test="${status=='BLOCKED'}">
                                        <c:if test="${fn:substring(accessResendUser.access, 2, 3)!='Y'}">
                                            <c:set var="visibleBttnResend" value="false" />
                                        </c:if>   
                                    </c:if>
                                    <c:if test="${status=='VIRUS'}">
                                        <c:if test="${fn:substring(accessResendUser.access, 3, 4)!='Y'}">
                                            <c:set var="visibleBttnResend" value="false" />
                                        </c:if>   
                                    </c:if>
                                </c:forEach>
                            </td>
                            <td>
                                <input id="bttn${mail.id}" type="button" value="Resend" 
                                       <c:if  test="${visibleBttnResend!='true'}">
                                           disabled="true" 
                                       </c:if>
                                       onclick="sendStatus(this.id);">
                            </td>
                        </tr> 
                    </c:forEach>
                </table>
            </div>
        </div>
    </body>

    <script>
        $('#id_datestart').datetimepicker();
        $('#id_dateend').datetimepicker();

        function sendStatus(value) {
            $('#' + value).attr('disabled', 'disabled');
            $.post(window.location.pathname + '/resend', {idmail: value.substring(4)});
        }
    </script>            


</body>
</html>
