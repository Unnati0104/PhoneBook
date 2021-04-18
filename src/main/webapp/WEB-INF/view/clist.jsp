<%-- 
    Document   : index
    Created on : 13 Apr, 2021, 3:54:35 PM
    Author     : Unnati
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%--to add a url we use taglib --%>
<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Contact List - Contact Application</title>
        <s:url var="url_css" value="/static/css/style.css" />
        <link href="${url_css}" rel="stylesheet" type="text/css" />
    </head>
    <%--<s:url var="url_bg" value="/static/images/bg-1.jpg"/>--%>
    <!--<body background="${url_bg}">-->
    <body>
        <table border="1" width="80%" align="center">
            <tr>
                <td height="80px">
                    <%-- Header --%>
                    <!--<h1>Contact Application</h1>-->
                    <jsp:include page="include/header.jsp" />
                </td>
                </tr>
                <tr>
                <td height="25px">
                    <%-- Menu --%>
                    <%--<jsp:include page="include/menu.jsp" />--%>
                    <s:url var="url_logout" value="/logout"/>
                    <s:url var="url_uhome" value="/user/dashboard"/>
                    <s:url var="url_cform" value="/user/contact_form"/>
                    <s:url var="url_clist" value="/user/clist"/>
                    <a href="${url_uhome}">Home</a> | <a href="${url_clist}">Contact List</a> | <a href="${url_cform}">Add Contact</a> | <a href="${url_logout}">Logout</a>  
                </td>
                </tr>
                <tr>
                <td height="350px" valign="top">
                    <%-- Page Content Area --%>
                    <h3>Contact List</h3>
                    <c:if test="${param.act eq 'sv'}">
                        <p class="success">Contact Saved Successfully</p>
                    </c:if>
                    <c:if test="${param.act eq 'del'}">
                        <p class="success">Contact Deleted Successfully</p>
                    </c:if>
                    
                    <table border = "1"  cellpadding="3"  width="100%">
                        <tr>
                            <th>SELECT</th>
                            <th>CID</th>
                            <th>NAME</th>
                            <th>PHONE</th>
                            <th>EMAIL</th>
                            <th>ADDRESS</th>
                            <th>REMARK</th>
                            <th>ACTION</th>
                         </tr>
                          <c:if test="${empty contactList}">
                                <tr>
                                    <td align="center" colspan="8" class="error">No Records Present</td>
                                </tr>
                            </c:if>
                         <c:forEach var="c" items="${contactList}" varStatus="st">
                             <tr>
                                <td>${st.count}</td>
                                <td>${c.contactId}</td>
                                <td>${c.name}</td>
                                <td>${c.phone}</td>
                                <td>${c.email}</td>
                                <td>${c.address}</td>
                                <td>${c.remark}</td>
                                <s:url var="url_del" value="/user/del_contact">
                                    <s:param name="cid" value="${c.contactId}"/>
                                </s:url>   
                                <s:url var="url_edit" value="/user/edit_contact">
                                    <s:param name="cid" value="${c.contactId}"/>
                                </s:url> 
                                    <td><a href="${url_edit}">Edit</a> | <a href="${url_del}">Delete</a></td>
                              </tr>
                         </c:forEach>
                    </table>
                </td>
                </tr>
                <tr>
                <td height="25px">
                    <%-- Footer --%>
                     <jsp:include page="include/footer.jsp" />
                </td>
            </tr>
        </table>
    </body>
</html>

