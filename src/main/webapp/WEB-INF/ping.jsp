<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page isELIgnored="false" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>故障机器列表</title>
</head>
<body>
<br>
<form action="${pageContext.request.contextPath}/ip/add" method="post">
	楼层：<input name="address">
	IP：<input name="host">
	<input type="submit" value="添加">
</form>
<br><br>
<form action="${pageContext.request.contextPath}/upload" method="post"
					enctype="multipart/form-data">
<input type="file" name="xls"/>
 <input type="submit" value="批量添加" />
</form>
<br><br>
	<table style="border-collapse: collapse;" border="1">
	<tr>
				<td>编号</td>
				<td>楼层</td>
				<td>IP</td>
				<td>状态</td>
				<td>日志</td>
				<td>操作</td>
			</tr>
		<c:forEach varStatus="status" var="ipaddress" items="${obj}">
			<tr>
				<td>${status.index+1}</td>
				<td>${ipaddress.address}&nbsp;&nbsp;&nbsp;</td>
				<td>${ipaddress.host}&nbsp;&nbsp;&nbsp;</td>
				<td><c:if test="${!ipaddress.status}"><label style="color: red;">不通</label></c:if>
							<c:if test="${ipaddress.status}"><label style="color: green;">正常</label></c:if>
				 </td>
				 <td>${ipaddress.log}</td>
				 <td><a href="javascript:if(confirm('确实要删除该地址吗?'))location='${pageContext.request.contextPath}/ip/delete?host=${ipaddress.host}'">删除</a></td>
			</tr>
		</c:forEach> 
	</table>
</body>
</html>