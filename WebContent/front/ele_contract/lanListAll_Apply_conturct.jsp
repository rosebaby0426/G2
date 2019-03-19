<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.goodhouse.member.model.*"%>
<%@ page import="com.goodhouse.apply_conturct.model.*"%>

<%
	MemVO mVO = (MemVO) session.getAttribute("memVO");
	List<Apply_ConturctVO> list = (List<Apply_ConturctVO>)session.getAttribute("applyConturctList");
	pageContext.setAttribute("list",list);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
<jsp:include page="/FrontHeaderFooter/Header.jsp" />
	<div class="container-fluid">
		<div lass="row justfy-content-center">
			<p>
				回首頁<a href="lan_select_page.jsp"><img src="<%=request.getContextPath()%>/share_pic/back1.gif"width="100" height="30 !important"></a>
			</p><br>
			<table class="table table-hover">
				<thead>
			    	<tr>
			      		<th scope="col">電子合約編號</th>
			      		<th scope="col">會員編號</th>
			      		<th scope="col">房屋編號</th>
			      		<th scope="col">申請選項</th>
			      		<th scope="col">申請狀態</th>
			      		<th scope="col">申請內容</th>
			      		<th scope="col"></th>
			    	</tr>
			  	</thead>
					<jsp:useBean id="conSvc" scope="page" class="com.goodhouse.contract.model.ContractService"></jsp:useBean>
					<jsp:useBean id="memSvc" scope="page" class="com.goodhouse.member.model.MemService"></jsp:useBean>
					<jsp:useBean id="houSvc" scope="page" class="com.goodhouse.house.model.HouseService"></jsp:useBean>
					<jsp:useBean id="appConSvc" scope="page" class="com.goodhouse.apply_conturct.model.Apply_ConturctService"></jsp:useBean>
					<jsp:useBean id="eleConSvc" scope="page" class="com.goodhouse.ele_contract.model.Ele_ContractService"></jsp:useBean>
				<%@ include file="page1.file"%>
			  	<tbody>
					<c:forEach var="appConVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">	
			    	<tr>
			      		<td>${appConVO.ele_con_id}</td>
				      	<td>${memSvc.getOneMem(appConVO.mem_id).mem_name}</td>
				      	<td>${houSvc.getOneHouse(appConVO.hou_id).hou_name}</td>
				      	
				      	<c:forEach var="AppConChoose" items="${Apply_ConturctChooseMap}">
								<c:if test="${AppConChoose.key eq appConVO.app_con_content}">
									<td>${AppConChoose.value.choose_name}</td>
								</c:if>
						</c:forEach>
						
				      	<c:forEach var="AppConStatus" items="${Apply_ConturctStatusMap}">
								<c:if test="${AppConStatus.key eq appConVO.app_con_status}">
									<td>${AppConStatus.value.status_name}</td>
								</c:if>
						</c:forEach>
						
				      	<td>${appConVO.app_con_other}</td>
				      	<td>
							<form method="post" action="apply_conturct.do">
								<input type="hidden" name="action" value="checkOK">
								<input type="hidden" name="app_con_id" value="${appConVO.app_con_id}">
								<input type="submit" value="確認處理" style='display:${(appConVO.app_con_status == "s1") ? "" : "none"}'>
							</form>
						</td>
			    	</tr>
					</c:forEach>
			  	</tbody>
				<%@ include file="page2.file"%>
			</table>
		</div>
	</div>

</body>
</html>