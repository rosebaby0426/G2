<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.apply_conturct.model.*"%>
<%@ page import="java.util.*"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<jsp:include page="/FrontHeaderFooter/Header.jsp" />
	<div style="margin-top:"></div>
	
	<div class="container-fluid">
		<div class="row justfy-content-center">
			<div class="row col-4">
				<%-- 錯誤表列 --%>
				<c:if test="${not empty errorMsgs}">
					<font style="color: red">請修正以下錯誤:</font>
					<ul>
						<c:forEach var="message" items="${errorMsgs}">
							<li style="color: red">${message}</li>
						</c:forEach>
					</ul>
				</c:if>
				<p>合約處理列表</p><br>
			</div>
			<div class="row col-8">
				<table>
					<tr>
						<th>合約處理編號</th>	
						<th>電子合約編號</th>	
						<th>會員編號</th>	
						<th>房屋編號</th>	
						<th>申請選項</th>	
						<th>申請狀態</th>	
						<th>申請備註</th>	
						
					</tr>
					<%
						List<Apply_ConturctVO> list = (List<Apply_ConturctVO>)session.getAttribute("applyConturctList");
						
						for(int i = 0 ; i < list.size() ; i++ ){
							
							Apply_ConturctVO appConVO = list.get(i);
							String app_con_id = appConVO.getApp_con_id();
							String ele_con_id = appConVO.getEle_con_id();
							String mem_id = appConVO.getMem_id();
							String hou_id = appConVO.getHou_id();
							String app_con_content = appConVO.getApp_con_content();
							String app_con_status = appConVO.getApp_con_status();
							String app_con_other = appConVO.getApp_con_other();
					%>
						<tr>
							<td><%= app_con_id%></td>
							<td><%= ele_con_id%></td>
							<td><%= mem_id%></td>
							<td><%= hou_id%></td>
							<td><%= app_con_content%></td>
							<td><%= app_con_status%></td>
							<td><%= app_con_other%></td>
						</tr>
					<%
						}
					%>
				</table>
			</div>
		</div>
	</div>
	
	
</body>
</html>