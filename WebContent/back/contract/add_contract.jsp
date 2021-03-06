<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.contract.model.*"%>

<%
  ContractVO conVO = (ContractVO) request.getAttribute("conVO");
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>合約分類新增資料</title>

<style>
  table#table-1 {
  	width: 800px;
	background-color: #CCCCFF;
    border: 2px solid black;
    text-align: center;
  }
  table#table-1 h4 {
  	width: 800px;
    color: red;
    display: block;
    margin-bottom: 1px;
  }
  h4 {
    color: blue;
    display: inline;
  }
  table {
	width: 800px;
	background-color: white;
	margin-top: 1px;
	margin-bottom: 1px;
  }
  table, th, td {
    border: 0px solid #CCCCFF;
  }
  th, td {
    padding: 1px;
  }
</style>

</head>
<body bgcolor='white'>
<jsp:include page="/FrontHeaderFooter/Header.jsp" />

	<table id="table-1">
		<tr><td>
			 <h3>合約分類新增 - add_contract.jsp</h3></td><td>
			 <h4><a href="select_page.jsp"><img src="images/tomcat.png" width="100" height="100" border="0">回首頁</a></h4>
		</td></tr>
	</table>
	
	<h3>資料新增:</h3>
	
	<c:if test="${not empty errorMsgs}">
		<font style="color:red">請修正以下錯誤:</font>
		<ul>
			<c:forEach var="message" items="${errorMsgs}">
				<li style="color:red">${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<form method="post" action="contract.do" name="form1">
		<table>
			<tr>
				<td size="45">合約分類名稱</td>
				<td>
					<input type="text" name="con_name" size="45" value="<%= (conVO == null)? "合約分類名稱" : conVO.getCon_name()%>" />
				</td>
			</tr>
			<tr>
				<td>合約內容</td>
				<td>
					<input type="text" name="con_content" size="45" value="<%= (conVO == null)?"合約內容" : conVO.getCon_content()%>"/>
				</td>
			</tr>
			<jsp:useBean id="conSvc" scope="page" class="com.goodhouse.contract.model.ContractService"/>
			<tr>
				<td>合約使用狀態</td>
				<td>
					<select name="con_status">
						<c:forEach var="con_status" items="${Con_statusList}">
							<option value="${con_status.status_name}" ${(con_status.status_no_name == conVO.con_status)?'selected':''}>${con_status.status_name}
						</c:forEach>
					</select>
				</td>
			</tr>
			
		</table>
		<input type="hidden" name="action" value="insert"/>
		<input type="submit" value="送出新增"/>
	
	</form>



</body>
</html>