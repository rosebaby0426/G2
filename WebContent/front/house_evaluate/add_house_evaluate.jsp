<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.house_evaluate.model.*"%>

<%
  House_EvaluateVO heVO = (House_EvaluateVO) request.getAttribute("House_EvaluateVO");
//House_EvaluateServlet.java (Concroller) 存入req的House_EvaluateVO物件 (包括幫忙取出的House_EvaluateVO 也包括輸入資料錯誤時的House_EvaluateVO物件)
%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<meta charset="UTF-8">
<title>新增房屋評價</title>
</head>
<body>
	
	<table id="table-1">
		<tr>
			<td><h3>房屋評價新增 - add_house_evaluate.jsp</h3></td>
			<td><h4><a href="select_page.jsp"><img src="images/tomcat.png" width="100" height="100" border="0">回首頁</a></h4></td>
		</tr>
	</table>
	
	<%-- 錯誤表列 --%>
	<c:if test="${not empty errorMsgs}">
		<font style="color:red">請修正以下錯誤:</font>
		<ul>
			<c:forEach var="message" items="${errorMsgs}">
				<li style="color:red">${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	
	<form method="post" action="house_evaluate.do" name="form1">
		<table>
			<tr>
				<td>會員編號</td>
				<td>
					<select >
						<c:forEach var="House_EvaluateVO" items="${heSvc.all}" > 
          				<option value="${heVO.hou_eva_id}">${empVO.ename}
         				</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>房屋編號</td>
			</tr>
			<tr>
				<td>評價等級</td>
			</tr>
			<tr>
				<td>評價內容</td>
			</tr>
		</table>
		
	</form>
</body>
</html>