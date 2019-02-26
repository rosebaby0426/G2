<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.ele_contract.model.*"%>
<%
	Ele_ContractVO eleConVO = (Ele_ContractVO) request.getAttribute("eleConVO");
%>

<!doctype html>
<html lang="en">
<head>
<!-- Required meta tags -->
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no">
<script src="<%=request.getContextPath()%>/file/jquery-1.12.4.min.js"></script>
<!-- Bootstrap CSS start-->
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/File/all.css"
	integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr"
	crossorigin="anonymous">
<link rel="stylesheet" href="<%=request.getContextPath()%>/File/all.css"
	integrity="sha384-fnmOCqbTlWIlj8LyTjo7mOUStjsKC4pOpQbqyi7RrhN7udi9RwhKkMHpvLbHG9Sr"
	crossorigin="anonymous">
<!-- Bootstrap CSS end-->
<title></title>
</head>
<body>
	<jsp:include page="/FrontHeaderFooter/Header.jsp" />
	<h1></h1>

	<!-- 工作區開始 -->
<div class="container">
	<div class="row justfy-content-center">
		<div class="row col-4">
			<table>
				<tr>
					<td>修改</td>
				</tr>
				<tr>
					<td>
						<a href="select_page.jsp"><img src="<%=request.getContextPath()%>/share_pic/back1.gif" width="100" height="32" border="0">回首頁</a>
					</td>
				</tr>
				<tr>
					<td>
						<%-- 錯誤表列 --%>
						<c:if test="${not empty errorMsgs}">
							<font style="color:red">請修正以下錯誤:</font>
							<ul>
								<c:forEach var="message" items="${errorMsgs}">
									<ul style="color:red">${message}</ul>
								</c:forEach>
							</ul>
						</c:if>
					</td>
				</tr>
			</table>
		</div>
		
		<div class="row col-8 ">
			<form method="post" action="ele_contract.do" name="form1">
						<table style="width:800px">
							<tr>
								<td>合約分類<font color=red><b>*</b></font></td>
								<td>
									<p><%=eleConVO.getCon_id()%></p>
								</td>
							</tr>
							<jsp:useBean id="memSvc" scope="page" class="com.goodhouse.member.model.MemService"/>
							<tr>
								<td>租屋者姓名(會員)<font color=red><b>*</b></font></td>
								<td>
									<p><%=eleConVO.getMem_id()%></p>
								</td>
							</tr>
							<tr>
								<td>租屋者身分證字號(會員)<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="mem_idnumber" value="<%=eleConVO.getMem_idnumber()%>"/>
								</td>
							</tr>
							<tr>
								<td>房東姓名<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="lan_id"  value=""/>
								</td>
							</tr>
							<tr>
								<td>房東身份證字號<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="lan_idnumber" value="<%=eleConVO.getLan_idnumber()%>"/>
								</td>
							</tr>
							<jsp:useBean id="houSvc" scope="page" class="com.goodhouse.house.model.HouseService"/>
							<!-- 
							<tr>
								<td>房屋<font color=red><b>*</b></font></td>
								<td>
									<select size="1" name="hou_id">
										<c:forEach var="houVO" items="${houSvc.all}">
											<option value="${houVO.hou_id}"${(eleConVO.lan_id == houVO.lan_id)? 'selected' : ''}>${houVO.hou_name}
										</c:forEach>
									</select>
								</td>
							</tr>
							 -->
							<tr>
								<td>房屋<font color=red><b>*</b></font></td>
								<td>
									<select size="1" name="hou_id" style="overflow:hidden; text-overflow:ellipsis;white-space:nowrap;width:225px;">
										<c:forEach var="houVO" items="${houSvc.all}">
											<option value="${houVO.hou_id}"}>${houVO.hou_name}
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td>每期租金<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="ele_rent_money" value="<%=eleConVO.getEle_rent_money()%>"/>
								</td>
							</tr>
							<tr>
								<td>押金<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="ele_deposit_money" value="<%=eleConVO.getEle_deposit_money()%>"/>
								</td>
							</tr>
							<tr>
								<td>租賃期限<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="ele_rent_time" value="<%=eleConVO.getEle_rent_time()%>"/>
								</td>
							</tr>
							<tr>
								<td>租賃起訖日<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="ele_rent_f_day" id="ele_rent_f_day"/>
								</td>
							</tr>
							<tr>
								<td>租賃結束日<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="ele_rent_l_day" id="ele_rent_l_day"/>
								</td>
							</tr>
							<tr>
								<td>簽約日期<font color=red><b>*</b></font></td>
								<td>
									<input type="text" name="ele_singdate" id="ele_singdate"/>
								</td>
							</tr>
							<tr>
								<td>繳費型態<font color=red><b>*</b></font></td>
								<td>
									<select name="bill_paymenttype" style="overflow:hidden; text-overflow:ellipsis;white-space:nowrap;width:225px;">
										<c:forEach var="bill_paymenttype" items="${Bill_PaymentTypeList}">
											<option value="${bill_paymenttype.type_no_name}" >${bill_paymenttype.type_name}
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td>合約備註</td>
								<td>
									<textarea name="ele_con_note" rows="3" cols="30" value="<%=eleConVO.getEle_con_note()%>"></textarea>
								</td>
							</tr>
						</table>
					</p>	
						<input type="hidden" name="action" value="update">
						<input type="hidden" name="ele_con_id" value="<%=eleConVO.getEle_con_id()%>">
						<input type="hidden" name="con_id" value="<%=eleConVO.getCon_id()%>"/>
						<input type="hidden" name="mem_id" value="<%=eleConVO.getMem_id()%>" />
						<input type="hidden" name="lan_id"  value="<%=eleConVO.getLan_id()%>"/>
						<input type="hidden" name="ele_con_status" value="<%=eleConVO.getEle_con_status()%>"/>
						<input type="submit" name="送出新增">
				</form>
		
		</div>
	
	
	</div>
</div>

<%

	java.sql.Date ele_rent_f_day = null;
	java.sql.Date ele_rent_l_day = null;
	java.sql.Date ele_singdate = null;
	
	try{
		ele_rent_f_day = eleConVO.getEle_rent_f_day();
		ele_rent_l_day = eleConVO.getEle_rent_l_day();
		ele_singdate = eleConVO.getEle_singdate();
	} catch (Exception e){
		ele_rent_f_day = new java.sql.Date(System.currentTimeMillis());
		ele_rent_l_day = new java.sql.Date(System.currentTimeMillis());
		ele_singdate = new java.sql.Date(System.currentTimeMillis());
	}

%>




	<!-- 工作區結束 -->

	<jsp:include page="/FrontHeaderFooter/Footer.jsp" />
	<!-- Optional JavaScript -->
	<!-- jQuery first, then Popper.js, then Bootstrap JS start-->
	<script
		src="<%=request.getContextPath()%>/bootstrap/jquery-3.3.1.slim.min.js"
		integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
		crossorigin="anonymous"></script>
	<script src="<%=request.getContextPath()%>/bootstrap/popper.min.js"
		integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut"
		crossorigin="anonymous"></script>
	<script
		src="<%=request.getContextPath()%>/bootstrap/js/bootstrap.min.js"></script>
	<!-- jQuery first, then Popper.js, then Bootstrap JS end-->

</body>
</html>