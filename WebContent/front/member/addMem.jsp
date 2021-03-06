<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.goodhouse.member.model.*"%>       

<%
  MemVO memVO = (MemVO) request.getAttribute("memVO");
%>
<html>
<head>

<meta charset="UTF-8">
<title>會員新增資料</title>

<style>
  table#table-1 {
	background-color: #CCCCFF;
    border: 2px solid black;
    text-align: center;
  }
  table#table-1 h4 {
    color: red;
    display: block;
    margin-bottom: 1px;
  }
  h4 {
    color: blue;
    display: inline;
  }
</style>

<style>
  table {
	width: 450px;
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
<jsp:include page="/FrontHeaderFooter/Header.jsp"/>	
<table id="table-1">
	<tr><td>
		 <h3>會員資料新增 - addMem.jsp</h3></td><td>
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






<FORM METHOD="post" ACTION="<%=request.getContextPath()%>/front/member/mem.do" name="form1" enctype="multipart/form-data">
<table>
	<tr>
		<td>會員姓名:</td>
		<td><input type="TEXT" name="mem_name" size="45" 
			 value="<%= (memVO==null)? "" : memVO.getMem_name()%>" /></td>
	</tr>
	<tr>
		<td>會員生日:</td>
		<td><input name="mem_birthday" id="f_date1" type="text"></td>
	</tr>
	<tr>
		<td>會員密碼:</td>
		<td><input type="password"  name="mem_password" size="45"
		value="<%= (memVO==null)?"" : memVO.getMem_password()%>"/></td>
		
	</tr>
	<tr>
		<td>會員地址:</td>
		<td><input type="TEXT" name="mem_address" size="45"
			 value="<%= (memVO==null)?""  : memVO.getMem_address()%>" /></td>
	</tr>
	<tr>
		<td>會員郵遞區號:</td>
		<td><input type="TEXT" name="mem_zipcode" size="45"
			 value="<%= (memVO==null)? "" : memVO.getMem_zipcode()%>" /></td>
	</tr>
	<tr>
		<td>會員電話:</td>
		<td><input type="TEXT" name="mem_telephone" size="45"
			 value="<%= (memVO==null)? "" : memVO.getMem_telephone()%>" /></td>
	</tr>
	<tr>
		<td>會員手機:</td>
		<td><input type="TEXT" name="mem_phone" size="45"
			 value="<%= (memVO==null)? "" : memVO.getMem_phone()%>" /></td>
	</tr>
	<tr>
		<td>會員信箱:</td>
		<td><input type="TEXT" name="mem_email" size="45"
			 value="<%= (memVO==null)? "" : memVO.getMem_email()%>" /></td>
	</tr>


	<tr>
		<td>會員狀態:</td>
		<td>
			<select name="mem_status">
				<option  value="1" selected>一般會員</option>
				
			</select>
		</td>
	</tr>


	<tr>
		<td>會員圖片:</td>
		<td><input type="file" name="mem_picture" size="45"	/></td>
	</tr>
	<tr>
		<td>積分總和:</td>
		<td><input type="TEXT" name="good_total" size="45"
			 value="<%= (memVO==null)? "" : memVO.getGood_total()%>" /></td>
	</tr>

	<tr>
		<td>會員性別:</td>
		<td>
			<select name="mem_sex">
				<option  value="1" selected>男</option>
				<option  value="2" >女</option>
			</select>
		</td>
	</tr>




</table>
<br>
<input type="hidden" name="action" value="insert">
<input type="submit" value="送出新增"></FORM>

<form METHOD="post" ACTION="<%=request.getContextPath()%>/front/member/mem.do">

<input type="hidden" name="action" value="buildMem">
<input type="submit" value="神奇小按鈕">

</form>






<jsp:include page="/FrontHeaderFooter/Footer.jsp"/>
</body>
<script src="datetimepicker/jquery.js"></script>
<script src="datetimepicker/jquery.datetimepicker.full.js"></script>
<link   rel="stylesheet" type="text/css" href="datetimepicker/jquery.datetimepicker.css" />
<script>
        $.datetimepicker.setLocale('zh'); // kr ko ja en
        $('#f_date1').datetimepicker({
           theme: '',          //theme: 'dark',
           timepicker: false,   //timepicker: false,
           step: 1,            //step: 60 (這是timepicker的預設間隔60分鐘)
	       format: 'Y-m-d ',
	       value: '',  
        });
 
</script>

</html>