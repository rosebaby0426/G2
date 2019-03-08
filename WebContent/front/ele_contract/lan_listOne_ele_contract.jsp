<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.ele_contract.model.*"%>
<%@ page import="com.goodhouse.member.model.*"%>
<%@ page import="com.goodhouse.house.model.*"%>
<%@ page import="com.goodhouse.landlord.model.*"%>
<%
	Ele_ContractVO eleConVO = (Ele_ContractVO) request.getAttribute("eleConVO");
		
%> 

<!doctype html>
<html lang="en">
<head>
</head>
<body>
	<jsp:include page="/FrontHeaderFooter/Header.jsp" />
	<h1></h1>

	<!-- 工作區開始 -->

	<div class="container">
		<div class="row justfy-content-center">
			<div class="row col-2">
				<p>回首頁<a href="lan_select_page.jsp"><img src="<%=request.getContextPath()%>/share_pic/back1.gif" width="100" height="30 !important" ></a></p><br>
				<c:if test="${not empty errorMsgs}">
					<font style="color:red">請修正以下錯誤:</font>
					<ul>
						<c:forEach var="message" items="${errorMsgs}">
							<li style="color:red">${message}</li>
						</c:forEach>
					</ul>
				</c:if>
			</div>
			
			<div class="row col-10">
				<table style="width:800px">
						<tr>
							<td>電子合約編號</td>
							<td>${eleConVO.ele_con_id}</td>
						</tr>
						<jsp:useBean id="conSvc" scope="page" class="com.goodhouse.contract.model.ContractService" />
						<tr>
							<td>合約分類名稱</td>
							<td>${conSvc.getOneCon(eleConVO.con_id).con_name}</td>
						</tr>
						<jsp:useBean id="mSvc" scope="page" class="com.goodhouse.member.model.MemService" />
						<tr>
							<td>房客姓名(會員)</td>
							<td>${mSvc.getOneMem(eleConVO.mem_id).mem_name}</td>
						</tr>
						<tr>
							<td>房客身分證字號(會員)</td>
							<td>${eleConVO.mem_idnumber}</td>
						</tr>
						<jsp:useBean id="lanSvc" scope="page" class="com.goodhouse.landlord.model.LanService" />
						<tr>
							<td>房東姓名</td>
							<td>${mSvc.getOneMem(lanSvc.getOneLan(eleConVO.lan_id).mem_id).mem_name}</td>
						</tr>
						<tr>
							<td>房東身份證字號</td>
							<td>${eleConVO.lan_idnumber}</td>
						</tr>
						<jsp:useBean id="houSvc" scope="page" class="com.goodhouse.house.model.HouseService" />
						<tr>
							<td>房屋</td>
							<td>${houSvc.getOneHouse(eleConVO.hou_id).hou_name}</td>
						</tr>
						<tr>
							<td>每期租金</td>
							<td>${eleConVO.ele_rent_money}</td>
						</tr>
						<tr>
							<td>押金</td>
							<td>${eleConVO.ele_deposit_money}</td>
						</tr>
						<tr>
							<td>租賃期限</td>
							<td>${eleConVO.ele_rent_time}</td>
						</tr>
						<tr>
							<td>租賃起訖日</td>
							<td>${eleConVO.ele_rent_f_day}</td>
						</tr>
						<tr>
							<td>租賃結束日</td>
							<td>${eleConVO.ele_rent_l_day}</td>
						</tr>
						<tr>
							<td>簽約日期</td>
							<td>${eleConVO.ele_singdate}</td>
						</tr>
						<tr>
							<td>合約狀態</td>
							<td>${eleConVO.ele_con_status}</td>
						</tr>
						<tr>
							<td>繳費型態</td>
							<td>${eleConVO.bill_paymenttype}</td>
						</tr>
						<tr>
							<td>合約備註</td>
							<td>${eleConVO.ele_con_note}</td>
						</tr>
					</table>
			</div>
		</div>
	
	</div>
	<!-- 工作區結束 -->

</body>
</html>