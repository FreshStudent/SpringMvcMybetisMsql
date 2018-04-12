<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'test.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript" src="<%=basePath%>js/jquery.js"></script>
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	<h3>利用表单传递数据</h3>
	<form name="form1" method="post" action="<%=basePath %>helloworld/fileUpload.spring">
		<p>
			你的姓名： <input type="text" name="name">
		</p>
		<p>
			你的爱好： <input type="text" name="hobby">
		</p>
		<p>
			你所从事的行业： <select name="work">
				<option></option>
				<!--默认为空，从下拉菜单中选择-->
				<option value="学生">学生</option>
				<option value="IT业">IT业</option>
				<option value="商业">商业</option>
				<option value="制造业">制造业</option>
				<option value="服务业">服务业</option>
			</select>
		</p>
		<p>
			<input type="button" value="提交" onclick="submit()"> <input type="Reset"
				value="重置">
		</p>
	</form>
</body>
</html>


