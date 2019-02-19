<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>房屋評價</title>

<style>
  table#table-1 {
	width: 450px;
	background-color: #CCCCFF;
	margin-top: 5px;
	margin-bottom: 10px;
    border: 3px ridge Gray;
    height: 80px;
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

</head>
<body bgcolor='white'>
	
	<tr><td><h3>GoodHouse House_Evaluate : Home</h3><h4>( MVC )</h4></td></tr>

	<h3>資料查詢:</h3>
	
	<c:if test="${not empty errorMsgs}">
		<font style="color:red">請修正以下錯誤:</font>
		<ul>
		    <c:forEach var="message" items="${errorMsgs}">
				<li style="color:red">${message}</li>
			</c:forEach>
		</ul>
	</c:if>
	
	<ul>
	  <li><a href='listAllHouse_Evaluate.jsp'>List</a> 所有評價列表  <br><br></li>
	  
	  
	  <li>
	    <FORM METHOD="post" ACTION="house_evaluate.do" >
	        <b>輸入房屋評價編號 (如EVA0000010):</b>
	        <input type="text" name="hou_eva_id">
	        <input type="hidden" name="action" value="getOne_For_Display">
	        <input type="submit" value="送出">
	    </FORM>
	  </li>
	
	  <jsp:useBean id="houEvaSvc" scope="page" class="com.goodhouse.house_evaluate.model.House_EvaluateService" />
	   
	  <li>
	     <FORM METHOD="post" ACTION="house_evaluate.do" >
	       <b>選擇員工編號:</b>
	       <select size="1" name="hou_eva_id">
	         <c:forEach var="heVO" items="${House_EvaluateService.all}" > 
	          <option value="${House_EvaluateVO.hou_eva_id}">${House_EvaluateVO.hou_eva_id}
	         </c:forEach>   
	       </select>
	       <input type="hidden" name="action" value="getOne_For_Display">
	       <input type="submit" value="送出">
	    </FORM>
	  </li>
	  
	  <li>
	     <FORM METHOD="post" ACTION="house_evaluate.do" >
	       <b>選擇評價者姓名:</b>
	       <select size="1" name="hou_eva_id">
	         <c:forEach var="House_EvaluateVO" items="${House_EvaluateService.all}" > 
	          <option value="${House_EvaluateVO.hou_eva_id}">${House_EvaluateVO.hou_eva_id}
	         </c:forEach>   
	       </select>
	       <input type="hidden" name="action" value="getOne_For_Display">
	       <input type="submit" value="送出">
	     </FORM>
	  </li>
	</ul>

<ul>
  <li><a href='addHouEva.jsp'>Add</a> 新增評價 </li>
</ul>

</body>
</html>