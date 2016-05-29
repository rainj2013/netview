<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>登陆</title>
<script type="text/javascript" src="jquery-1.12.4.min.js"></script>
</head>
<body>
<input name = "password" id="password">
<button id = "sub">登陆</button>
<script>
$("#sub").click(function(){
	$.ajax({ url: "login",type:'post',
		data:{
			"password":$("#password").val()
			},
		cache:false,
		success: function(result){
     		if(result){
     			window.location.href="view";
     		}else{
     			alert("密码错误！");
     		}
      }});
})
</script>
</body>
</html>