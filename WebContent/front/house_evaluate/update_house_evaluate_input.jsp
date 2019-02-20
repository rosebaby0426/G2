<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.house_evaluate.model.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>房屋評價修改</title>
</head>
<body bgcolor='white'>
	
	<table id="table-1">
		<tr>
			<td>
				<h3>房屋評價修改 - update_house_evaluate_input.jsp</h3>
				<h4><a href="select_page.jsp"><img src="images/back1.gif" width="100" height="32" border="0">回首頁</a></h4>
			</td>
		</tr>
	</table>
	
	<h3>資料修改:</h3>

	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<font style="color:red">請修正以下錯誤:</font>
		<ul>
			<c:forEach var="message" items="${errorMsgs}">
				<li style="color:red">${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<form>
		<table>
			<tr>
				<td></td>
			</tr>
		</table>
	</form>
</body>
</html>